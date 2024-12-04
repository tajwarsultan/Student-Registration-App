import socket
import sys

class StudentRegistrationClient:
    def __init__(self, host='localhost', port=9876):
        self.host = host
        self.port = port
        self.socket = None

    def connect(self):
        """Establish connection to the server."""
        try:
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.socket.connect((self.host, self.port))
            print(f"Connected to server at {self.host}:{self.port}")
        except Exception as e:
            print(f"Connection error: {e}")
            sys.exit(1)

    def send_request(self, request):
        """Send a request to the server and receive response."""
        try:
            # Ensure request ends with newline for server-side readLine()
            self.socket.sendall((request + '\n').encode())
            
            # Receive response
            response = self.socket.recv(1024).decode().strip()
            return response
        except Exception as e:
            print(f"Send/Receive error: {e}")
            return None

    def register_student(self, name, email, reg_id, marks):
        """Register a new student."""
        request = f"REGISTER|{name}|{email}|{reg_id}|{marks}"
        response = self.send_request(request)
        
        if response and response.startswith("SUCCESS"):
            student_id = response.split('|')[1]
            print(f"Student registered successfully with ID: {student_id}")
        else:
            print("Registration failed.")

    def get_student(self, student_id):
        """Retrieve student details."""
        request = f"GET|{student_id}"
        response = self.send_request(request)
        
        if response and response.startswith("SUCCESS"):
            # Parse student details
            _, id, name, email, reg_id, marks = response.split('|')
            print("\nStudent Details:")
            print(f"ID: {id}")
            print(f"Name: {name}")
            print(f"Email: {email}")
            print(f"Registration ID: {reg_id}")
            print(f"Marks: {marks}")
            return name, email, reg_id, marks
        else:
            print("Student retrieval failed.")
            return None

    def update_student(self, student_id, name=None, email=None, reg_id=None, marks=None):
        """Update student details."""
        # If no specific update fields are provided, first retrieve existing student details
        if all(v is None for v in [name, email, reg_id, marks]):
            existing_details = self.get_student(student_id)
            if not existing_details:
                return
            
            name, email, reg_id, marks = existing_details
            
            # Prompt for updates
            print("\nEnter new details (leave blank to keep existing)")
            new_name = input(f"Name [{name}]: ") or name
            new_email = input(f"Email [{email}]: ") or email
            new_reg_id = input(f"Registration ID [{reg_id}]: ") or reg_id
            
            while True:
                new_marks_input = input(f"Marks [{marks}]: ") or str(marks)
                try:
                    new_marks = float(new_marks_input)
                    break
                except ValueError:
                    print("Invalid marks. Please enter a numeric value.")
        else:
            # Use provided values
            new_name = name
            new_email = email
            new_reg_id = reg_id
            new_marks = marks

        # Construct update request
        request = f"UPDATE|{student_id}|{new_name}|{new_email}|{new_reg_id}|{new_marks}"
        response = self.send_request(request)
        
        if response and response.startswith("SUCCESS"):
            print("Student details updated successfully.")
        else:
            print("Update failed.")

    def delete_student(self, student_id):
        """Delete a student record."""
        request = f"DELETE|{student_id}"
        response = self.send_request(request)
        
        if response and response.startswith("SUCCESS"):
            print("Student deleted successfully.")
        else:
            print("Deletion failed.")

    def interactive_menu(self):
        """Display interactive menu for user interactions."""
        while True:
            print("\n--- Student Registration System ---")
            print("1. Register Student")
            print("2. Get Student Details")
            print("3. Update Student")
            print("4. Delete Student")
            print("5. Exit")
            
            choice = input("Enter your choice (1-5): ")
            
            if choice == '1':
                name = input("Enter student name: ")
                email = input("Enter student email: ")
                reg_id = input("Enter student registration ID: ")
                
                while True:
                    try:
                        marks = float(input("Enter student marks: "))
                        break
                    except ValueError:
                        print("Invalid marks. Please enter a numeric value.")
                
                self.register_student(name, email, reg_id, marks)
            
            elif choice == '2':
                student_id = input("Enter student ID: ")
                self.get_student(student_id)
            
            elif choice == '3':
                student_id = input("Enter student ID to update: ")
                self.update_student(student_id)
            
            elif choice == '4':
                student_id = input("Enter student ID to delete: ")
                self.delete_student(student_id)
            
            elif choice == '5':
                print("Exiting...")
                break
            
            else:
                print("Invalid choice. Please try again.")

def main():
    client = StudentRegistrationClient()
    try:
        client.connect()
        client.interactive_menu()
    finally:
        if client.socket:
            client.socket.close()

if __name__ == "__main__":
    main()