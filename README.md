# Student Registration Application Documentation

## Overview
The Student Registration Application is a client-server system designed to manage student records, providing basic CRUD (Create, Read, Update, Delete) operations through a socket-based communication protocol.

## System Architecture
- **Server**: Java-based server running on localhost
- **Client**: Python-based client 
- **Communication**: Socket-based network communication
- **Protocol**: Custom text-based protocol over TCP

## Key Components

### 1. Server-Side (Java)
#### Responsibilities
- Handle student registration
- Store and manage student records
- Process client requests
- Provide thread-safe operations

#### Supported Operations
1. **REGISTER**: Add a new student
   - Format: `REGISTER|name|email|regId|marks`
   - Returns a unique student ID on successful registration

2. **GET**: Retrieve student details
   - Format: `GET|studentId`
   - Returns complete student information

3. **DELETE**: Remove a student record
   - Format: `DELETE|studentId`
   - Removes student from the database

### 2. Client-Side (Python)
#### Features
- Interactive command-line interface
- Network communication with server
- Error handling
- Input validation

#### Menu Options
1. Register Student
2. Get Student Details
3. Delete Student
4. Exit

## Communication Protocol
### Request Format
- Requests are sent as pipe-separated (`|`) strings
- Each request type has a specific format
- Requests end with a newline character

### Response Format
- **SUCCESS** responses include requested information
- **ERROR** responses provide failure details

## Technical Details

### Connection Mechanism
- Uses socket programming
- Server listens on localhost:9876
- Client establishes a connection to the server
- Supports concurrent client connections

### Error Handling
- Connection errors
- Invalid input handling
- Server-side and client-side validation

## Installation (macOS)

### Prerequisites
- Java Development Kit (JDK)
- Python 3.x
- Socket library

### Setup Steps
1. **Install Java**
   ```bash
   # Using Homebrew
   brew install openjdk
   ```

2. **Prepare Python Environment**
   ```bash
   # Ensure Python 3 is installed
   python3 --version
   ```

3. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd student-registration-app
   ```

4. **Compile Java Server**
   ```bash
   javac StudentServer.java
   ```

5. **Run the Server**
   ```bash
   java StudentServer
   ```

6. **Run the Python Client**
   ```bash
   python3 student_client.py
   ```

## Usage Example

1. Start the Java server
2. Launch the Python client
3. Choose menu options:
   - Register a new student
   - Retrieve student details
   - Delete a student record

## Limitations
- No persistent storage
- Basic authentication
- Limited to local network communication

## Security Considerations
- Implement proper input sanitization
- Add authentication mechanism
- Use encrypted communication for production

## Future Improvements
- Database integration
- Web-based interface
- Advanced authentication
- Persistent storage
- Enhanced error handling

## Troubleshooting
- Ensure both server and client are on the same network
- Check firewall settings
- Verify port availability
- Confirm Java and Python versions

## Contact and Support
For issues or contributions, please contact the project maintainers.

## License
[Specify your license here]# Student-Registration-App
