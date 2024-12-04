import java.io.*;
import java.net.*;
import java.util.*;

public class StudentServer {
    // Use a synchronized map for thread safety
    private static Map<Integer, Student> studentDatabase = Collections.synchronizedMap(new HashMap<>());
    private static int nextStudentId = 1000;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9876);
            System.out.println("Student Registration Server is running on port 9876");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Handle each client in a separate thread
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle individual client connections
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                // Setup input and output streams
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Continuously handle client requests
                while (true) {
                    String request = in.readLine();
                    if (request == null) break;

                    // Process different types of requests
                    String[] parts = request.split("\\|");
                    String operation = parts[0];

                    switch (operation) {
                        case "REGISTER":
                            handleRegister(parts);
                            break;
                        case "GET":
                            handleGet(parts);
                            break;
                        case "UPDATE":
                            handleUpdate(parts);
                            break;
                        case "DELETE":
                            handleDelete(parts);
                            break;
                        case "EXIT":
                            return;
                        default:
                            out.println("ERROR|Invalid operation");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleRegister(String[] parts) {
            // REGISTER|name|email|regId|marks
            if (parts.length < 5) {
                out.println("ERROR|Invalid registration data");
                return;
            }

            try {
                Student student = new Student(
                    nextStudentId++, 
                    parts[1],   // name
                    parts[2],   // email
                    parts[3],   // regId
                    Double.parseDouble(parts[4])  // marks
                );

                studentDatabase.put(student.getId(), student);
                out.println("SUCCESS|" + student.getId());
            } catch (NumberFormatException e) {
                out.println("ERROR|Invalid marks format");
            }
        }

        private void handleGet(String[] parts) {
            if (parts.length < 2) {
                out.println("ERROR|Invalid student ID");
                return;
            }

            try {
                int studentId = Integer.parseInt(parts[1]);
                Student student = studentDatabase.get(studentId);

                if (student != null) {
                    out.println("SUCCESS|" + student.toString());
                } else {
                    out.println("ERROR|Student not found");
                }
            } catch (NumberFormatException e) {
                out.println("ERROR|Invalid student ID format");
            }
        }

        private void handleUpdate(String[] parts) {
            // UPDATE|studentId|name|email|regId|marks
            if (parts.length < 6) {
                out.println("ERROR|Invalid update data");
                return;
            }

            try {
                int studentId = Integer.parseInt(parts[1]);
                Student existingStudent = studentDatabase.get(studentId);

                if (existingStudent != null) {
                    // Update student details
                    Student updatedStudent = new Student(
                        studentId,
                        parts[2],   // name
                        parts[3],   // email
                        parts[4],   // regId
                        Double.parseDouble(parts[5])  // marks
                    );

                    studentDatabase.put(studentId, updatedStudent);
                    out.println("SUCCESS|Student updated");
                } else {
                    out.println("ERROR|Student not found");
                }
            } catch (NumberFormatException e) {
                out.println("ERROR|Invalid marks format");
            } catch (Exception e) {
                out.println("ERROR|Update failed");
            }
        }

        private void handleDelete(String[] parts) {
            if (parts.length < 2) {
                out.println("ERROR|Invalid student ID");
                return;
            }

            try {
                int studentId = Integer.parseInt(parts[1]);
                if (studentDatabase.remove(studentId) != null) {
                    out.println("SUCCESS|Student deleted");
                } else {
                    out.println("ERROR|Student not found");
                }
            } catch (NumberFormatException e) {
                out.println("ERROR|Invalid student ID format");
            }
        }
    }

    // Student class to represent student data
    private static class Student {
        private int id;
        private String name;
        private String email;
        private String regId;
        private double marks;

        public Student(int id, String name, String email, String regId, double marks) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.regId = regId;
            this.marks = marks;
        }

        public int getId() { return id; }

        @Override
        public String toString() {
            return id + "|" + name + "|" + email + "|" + regId + "|" + marks;
        }
    }
}