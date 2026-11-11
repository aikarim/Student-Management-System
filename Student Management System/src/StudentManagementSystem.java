import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Person implements Serializable {
    private String name;
    private String contact;
    private String address;

    public Person(String name, String contact, String address) {
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

class Student extends Person {
    private String id;
    private String department;
    private String semester;

    public Student(String id, String name, String department, String semester, String contact, String address) {
        super(name, contact, address);
        this.id = id;
        this.department = department;
        this.semester = semester;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}

class StudentManager {
    private List<Student> students;
    private final String FILE_NAME = "students.dat";

    public StudentManager() {
        students = new ArrayList<>();
        loadStudents();
    }

    public void addStudent(Student s) {
        students.add(s);
        saveStudents();
    }

    public void updateStudent(int index, Student s) {
        students.set(index, s);
        saveStudents();
    }

    public void deleteStudent(int index) {
        students.remove(index);
        saveStudents();
    }

    public List<Student> getStudents() {
        return students;
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (Exception ignored) {}
    }

    @SuppressWarnings("unchecked")
    private void loadStudents() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (List<Student>) ois.readObject();
        } catch (Exception ignored) {}
    }
}

public class StudentManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

class LoginFrame extends JFrame {
    private JTextField userText;
    private JPasswordField passText;

    public LoginFrame() {
        setTitle("Admin Login");
        setSize(320, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 5, 10));

        add(new JLabel("  Username:"));
        userText = new JTextField();
        add(userText);

        add(new JLabel("  Password:"));
        passText = new JPasswordField();
        add(passText);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (userText.getText().equals("admin") && new String(passText.getPassword()).equals("admin123")) {
                    dispose();
                    new MainFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(new JLabel(""));
        add(loginButton);
    }
}

class MainFrame extends JFrame {
    private StudentManager manager;
    private JTable table;
    private DefaultTableModel model;
    private JTextField idField, nameField, deptField, semField, contactField, addressField;

    public MainFrame() {
        manager = new StudentManager();
        setTitle("Student Management System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Department:"));
        deptField = new JTextField();
        inputPanel.add(deptField);

        inputPanel.add(new JLabel("Semester:"));
        semField = new JTextField();
        inputPanel.add(semField);

        inputPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        inputPanel.add(contactField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Department", "Semester", "Contact", "Address"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idField.setText(model.getValueAt(row, 0).toString());
                    nameField.setText(model.getValueAt(row, 1).toString());
                    deptField.setText(model.getValueAt(row, 2).toString());
                    semField.setText(model.getValueAt(row, 3).toString());
                    contactField.setText(model.getValueAt(row, 4).toString());
                    addressField.setText(model.getValueAt(row, 5).toString());
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton searchButton = new JButton("Search by ID");
        JButton clearButton = new JButton("Clear Fields");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            Student s = createStudentFromFields();
            if (s != null) {
                manager.addStudent(s);
                refreshTable();
                clearFields();
            }
        });

        updateButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Student s = createStudentFromFields();
                if (s != null) {
                    manager.updateStudent(row, s);
                    refreshTable();
                    clearFields();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a row to update.");
            }
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                manager.deleteStudent(row);
                refreshTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Select a row to delete.");
            }
        });

        searchButton.addActionListener(e -> {
            String searchId = JOptionPane.showInputDialog(this, "Enter Student ID to search:");
            if (searchId != null && !searchId.trim().isEmpty()) {
                boolean found = false;
                for (int i = 0; i < manager.getStudents().size(); i++) {
                    if (manager.getStudents().get(i).getId().equalsIgnoreCase(searchId.trim())) {
                        table.setRowSelectionInterval(i, i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(this, "Student not found.");
                }
            }
        });

        clearButton.addActionListener(e -> clearFields());

        refreshTable();
    }

    private Student createStudentFromFields() {
        if (idField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID and Name are required fields.");
            return null;
        }
        return new Student(
                idField.getText().trim(),
                nameField.getText().trim(),
                deptField.getText().trim(),
                semField.getText().trim(),
                contactField.getText().trim(),
                addressField.getText().trim()
        );
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Student s : manager.getStudents()) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getDepartment(),
                    s.getSemester(),
                    s.getContact(),
                    s.getAddress()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        deptField.setText("");
        semField.setText("");
        contactField.setText("");
        addressField.setText("");
        table.clearSelection();
    }
}