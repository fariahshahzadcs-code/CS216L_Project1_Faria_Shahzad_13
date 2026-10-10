double gpa;

    public Student(int id, String name, double gpa) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
    }

    public void display() {
        System.out.printf("ID: %-5d | Name: %-15s | GPA: %.2f%n", id, name, gpa);
    }
}

// Node for Singly Linked List
class Node {
    Student data;
    Node next;

    public Node(Student data) {
        this.data = data;
        this.next = null;
    }
}

// Custom Singly Linked List for managing records
class StudentLinkedList {
    private Node head;

    public StudentLinkedList() {
        this.head = null;
    }

    // Insert student record at the end
    public void insert(Student s) {
        Node newNode = new Node(s);
        if (head == null) {
            head = newNode;
            return;
        }
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    // Delete record by ID and return deleted student for undo stack
    public Student deleteById(int id) {
        if (head == null) return null;

        if (head.data.id == id) {
            Student deletedStudent = head.data;
            head = head.next;
            return deletedStudent;
        }

        Node current = head;
        while (current.next != null && current.next.data.id != id) {
            current = current.next;
        }

        if (current.next != null) {
            Student deletedStudent = current.next.data;
            current.next = current.next.next;
            return deletedStudent;
        }

        return null;
    }

    // Linear Search by Student ID
    public Student linearSearch(int id) {
        Node current = head;
        while (current != null) {
            if (current.data.id == id) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    // Bubble Sort by GPA in descending order
    public void bubbleSortByGpa() {
        if (head == null || head.next == null) return;

        boolean swapped;
        Node current;
        Node lastPtr = null;

        do {
            swapped = false;
            current = head;

            while (current.next != lastPtr) {
                if (current.data.gpa < current.next.data.gpa) {
                    Student temp = current.data;
                    current.data = current.next.data;
                    current.next.data = temp;
                    swapped = true;
                }
                current = current.next;
            }
            lastPtr = current;
        } while (swapped);
    }

    // Display all student records
    public void displayAll() {
        if (head == null) {
            System.out.println("No records found. The list is currently empty.");
            return;
        }
        System.out.println("-------------------------------------------");
        System.out.printf("%-8s | %-15s | %-6s%n", "ID", "Name", "GPA");
        System.out.println("-------------------------------------------");
        Node current = head;
        while (current != null) {
            current.data.display();
            current = current.next;
        }
        System.out.println("-------------------------------------------");
    }

    public boolean isEmpty() {
        return head == null;
    }
}

// Node for custom Stack implementation
class StackNode {
    Student data;
    StackNode next;

    public StackNode(Student data) {
        this.data = data;
        this.next = null;
    }
}

// Custom Stack for Undo functionality
class StudentStack {
    private StackNode top;

    public StudentStack() {
        this.top = null;
    }

    public void push(Student s) {
        StackNode newNode = new StackNode(s);
        newNode.next = top;
        top = newNode;
    }

    public Student pop() {
        if (isEmpty()) return null;
        Student poppedData = top.data;
        top = top.next;
        return poppedData;
    }

    public Student peek() {
        if (isEmpty()) return null;
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }
}

// Main Driver Class with Menu-Driven Interface
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentLinkedList studentList = new StudentLinkedList();
        StudentStack undoStack = new StudentStack();

        while (true) {
            System.out.println("\n=== STUDENT RECORD SYSTEM ===");
            System.out.println("1. Add Student Record");
            System.out.println("2. Delete Student Record (by ID)");
            System.out.println("3. Search Student (Linear Search)");
            System.out.println("4. Sort Students by GPA (Bubble Sort)");
            System.out.println("5. Undo Last Deleted Record");
            System.out.println("6. Display All Records");
            System.out.println("7. Exit");
            System.out.print("Select an option (1-7): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid ID. Enter an integer: ");
                        scanner.nextLine();
                    }
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    if (studentList.linearSearch(id) != null) {
                        System.out.println("Error: Student with ID " + id + " already exists.");
                        break;
                    }

                    System.out.print("Enter Student Name: ");
                    String name = scanner.nextLine().trim();

                    System.out.print("Enter Student GPA (0.00 - 4.00): ");
                    while (!scanner.hasNextDouble()) {
                        System.out.print("Invalid GPA. Enter a decimal number: ");
                        scanner.nextLine();
                    }
                    double gpa = scanner.nextDouble();
                    scanner.nextLine();

                    if (gpa < 0.0 || gpa > 4.0) {
                        System.out.println("Error: GPA must be between 0.00 and 4.00.");
                        break;
                    }

                    studentList.insert(new Student(id, name, gpa));
                    System.out.println("Student record added successfully.");
                    break;

                case 2:
                    if (studentList.isEmpty()) {
                        System.out.println("List is empty. Nothing to delete.");
                        break;
                    }
                    System.out.print("Enter Student ID to delete: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid ID. Enter an integer: ");
                        scanner.nextLine();
                    }
                    int delId = scanner.nextInt();
                    scanner.nextLine();

                    Student deleted = studentList.deleteById(delId);
                    if (deleted != null) {
                        undoStack.push(deleted);
                        System.out.println("Student with ID " + delId + " deleted successfully. (Added to Undo stack)");
                    } else {
                        System.out.println("Student ID " + delId + " not found.");
                    }
                    break;

                case 3:
                    if (studentList.isEmpty()) {
                        System.out.println("List is empty.");
                        break;
                    }
                    System.out.print("Enter Student ID to search: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid ID. Enter an integer: ");
                        scanner.nextLine();
                    }
                    int searchId = scanner.nextInt();
                    scanner.nextLine();

                    Student found = studentList.linearSearch(searchId);
                    if (found != null) {
                        System.out.println("Record found:");
                        found.display();
                    } else {
                        System.out.println("Student with ID " + searchId + " not found.");
                    }
                    break;

                case 4:
                    if (studentList.isEmpty()) {
                        System.out.println("List is empty. Nothing to sort.");
                        break;
                    }
                    studentList.bubbleSortByGpa();
                    System.out.println("Records sorted by GPA (Highest to Lowest) successfully.");
                    studentList.displayAll();
                    break;

                case 5:
                    if (undoStack.isEmpty()) {
                        System.out.println("Undo stack is empty. No deleted record to restore.");
                    } else {
                        Student restored = undoStack.pop();
                        studentList.insert(restored);
                        System.out.println("Restored record: ID " + restored.id + " (" + restored.name + ")");
                    }
                    break;

                case 6:
                    studentList.displayAll();
                    break;

                case 7:
                    System.out.println("Exiting application.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid selection. Choose between 1 and 7.");
            }
        }
    }
}