package com.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentManagementUI extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfId, tfName, tfAge, tfGender, tfScore;
    private StudentDAO dao = new StudentDAO();

    public StudentManagementUI() {
        setTitle("学生信息管理系统");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadData();
    }

    private void initUI() {
        // ---- 表格区域 ----
        String[] columns = {"ID", "姓名", "年龄", "性别", "成绩"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
        JScrollPane scrollPane = new JScrollPane(table);

        // ---- 表单区域 ----
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("学生信息"));
        tfId     = new JTextField(); tfId.setEditable(false);
        tfName   = new JTextField();
        tfAge    = new JTextField();
        tfGender = new JTextField();
        tfScore  = new JTextField();
        formPanel.add(new JLabel("ID（自动）:")); formPanel.add(tfId);
        formPanel.add(new JLabel("姓名:"));       formPanel.add(tfName);
        formPanel.add(new JLabel("年龄:"));       formPanel.add(tfAge);
        formPanel.add(new JLabel("性别:"));       formPanel.add(tfGender);
        formPanel.add(new JLabel("成绩:"));       formPanel.add(tfScore);

        // ---- 按钮区域 ----
        JButton btnAdd    = new JButton("添加");
        JButton btnUpdate = new JButton("修改");
        JButton btnDelete = new JButton("删除");
        JButton btnClear  = new JButton("清空");
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnClear);

        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());

        // ---- 右侧面板 ----
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(220, 0));

        // ---- 整体布局 ----
        setLayout(new BorderLayout(10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Student> list = dao.findAll();
        for (Student s : list) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getAge(), s.getGender(), s.getScore()});
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        tfId.setText(tableModel.getValueAt(row, 0).toString());
        tfName.setText(tableModel.getValueAt(row, 1).toString());
        tfAge.setText(tableModel.getValueAt(row, 2).toString());
        tfGender.setText(tableModel.getValueAt(row, 3).toString());
        tfScore.setText(tableModel.getValueAt(row, 4).toString());
    }

    private void addStudent() {
        try {
            Student s = buildStudentFromForm();
            if (dao.add(s)) {
                JOptionPane.showMessageDialog(this, "添加成功！");
                loadData(); clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入有误：" + ex.getMessage());
        }
    }

    private void updateStudent() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先从表格中选择要修改的学生");
            return;
        }
        try {
            Student s = buildStudentFromForm();
            s.setId(Integer.parseInt(tfId.getText()));
            if (dao.update(s)) {
                JOptionPane.showMessageDialog(this, "修改成功！");
                loadData(); clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入有误：" + ex.getMessage());
        }
    }

    private void deleteStudent() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先从表格中选择要删除的学生");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "确认删除该学生？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dao.delete(Integer.parseInt(tfId.getText()));
            JOptionPane.showMessageDialog(this, "删除成功！");
            loadData(); clearForm();
        }
    }

    private Student buildStudentFromForm() {
        String name   = tfName.getText().trim();
        int age       = Integer.parseInt(tfAge.getText().trim());
        String gender = tfGender.getText().trim();
        double score  = Double.parseDouble(tfScore.getText().trim());
        if (name.isEmpty() || gender.isEmpty()) throw new IllegalArgumentException("姓名和性别不能为空");
        return new Student(0, name, age, gender, score);
    }

    private void clearForm() {
        tfId.setText(""); tfName.setText(""); tfAge.setText("");
        tfGender.setText(""); tfScore.setText("");
        table.clearSelection();
    }
}