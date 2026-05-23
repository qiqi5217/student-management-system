package com.student;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class StudentManagementUI extends JFrame {

    // 配色
    private static final Color PRIMARY   = new Color(70, 130, 180);
    private static final Color BTN_ADD   = new Color(70, 160, 100);
    private static final Color BTN_UPD   = new Color(70, 130, 180);
    private static final Color BTN_DEL   = new Color(200, 80, 80);
    private static final Color BTN_CLR   = new Color(130, 130, 130);
    private static final Color ROW_ALT   = new Color(240, 246, 255);
    private static final Color BG        = new Color(245, 247, 250);

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfId, tfName, tfAge, tfGender, tfScore, tfMajor, tfSearch;
    private StudentDAO dao = new StudentDAO();

    public StudentManagementUI() {
        setTitle("学生信息管理系统");
        setSize(900, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        initUI();
        loadData(null);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // ===== 左侧：表单 + 按钮 =====
        JPanel left = new JPanel(new BorderLayout(0, 10));
        left.setBackground(BG);
        left.setPreferredSize(new Dimension(260, 0));
        left.setBorder(new EmptyBorder(10, 12, 10, 6));

        // 表单
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 220, 235), 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 4, 5, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        tfId     = styledField(); tfId.setEditable(false);
        tfName   = styledField();
        tfAge    = styledField();
        tfGender = styledField();
        tfScore  = styledField();
        tfMajor  = styledField();

        addFormRow(form, c, 0, "ID（自动）", tfId);
        addFormRow(form, c, 1, "姓　　名", tfName);
        addFormRow(form, c, 2, "年　　龄", tfAge);
        addFormRow(form, c, 3, "性　　别", tfGender);
        addFormRow(form, c, 4, "成　　绩", tfScore);
        addFormRow(form, c, 5, "专　　业", tfMajor);

        // 按钮
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setBackground(BG);
        JButton btnAdd  = makeBtn("➕ 添加", BTN_ADD);
        JButton btnUpd  = makeBtn("✏️ 修改", BTN_UPD);
        JButton btnDel  = makeBtn("🗑 删除", BTN_DEL);
        JButton btnClr  = makeBtn("↩ 清空", BTN_CLR);
        btnPanel.add(btnAdd); btnPanel.add(btnUpd);
        btnPanel.add(btnDel); btnPanel.add(btnClr);

        btnAdd.addActionListener(e -> addStudent());
        btnUpd.addActionListener(e -> updateStudent());
        btnDel.addActionListener(e -> deleteStudent());
        btnClr.addActionListener(e -> clearForm());

        left.add(form, BorderLayout.CENTER);
        left.add(btnPanel, BorderLayout.SOUTH);

        // ===== 右侧：搜索 + 表格 =====
        JPanel right = new JPanel(new BorderLayout(0, 8));
        right.setBackground(BG);
        right.setBorder(new EmptyBorder(10, 6, 10, 12));

        // 搜索栏
        JPanel searchBar = new JPanel(new BorderLayout(6, 0));
        searchBar.setBackground(BG);
        tfSearch = styledField();
        tfSearch.putClientProperty("JTextField.placeholderText", "输入姓名搜索...");
        JButton btnSearch = makeBtn("🔍 查询", PRIMARY);
        JButton btnReset  = makeBtn("全部", BTN_CLR);
        btnSearch.setPreferredSize(new Dimension(90, 32));
        btnReset.setPreferredSize(new Dimension(60, 32));
        JPanel searchBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        searchBtns.setBackground(BG);
        searchBtns.add(btnSearch); searchBtns.add(btnReset);
        searchBar.add(new JLabel("按姓名查询："), BorderLayout.WEST);
        searchBar.add(tfSearch, BorderLayout.CENTER);
        searchBar.add(searchBtns, BorderLayout.EAST);
        btnSearch.addActionListener(e -> loadData(tfSearch.getText().trim()));
        btnReset.addActionListener(e -> { tfSearch.setText(""); loadData(null); });

        // 表格
        String[] cols = {"ID", "姓名", "年龄", "性别", "成绩", "专业"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(180, 210, 245));

        // 隔行变色
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int r, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, col);
                setHorizontalAlignment(CENTER);
                if (!sel) setBackground(r % 2 == 0 ? Color.WHITE : ROW_ALT);
                return this;
            }
        });

        // 表头样式
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 13));
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 32));

        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(210, 220, 235), 1, true));

        right.add(searchBar, BorderLayout.NORTH);
        right.add(scroll, BorderLayout.CENTER);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
    }

    // ===== 辅助方法 =====
    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(0, 30));
        return f;
    }

    private void addFormRow(JPanel p, GridBagConstraints c, int row, String label, JTextField field) {
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        JLabel lbl = new JLabel(label + "：");
        lbl.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        p.add(lbl, c);
        c.gridx = 1; c.weightx = 1;
        p.add(field, c);
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 34));
        return btn;
    }

    private void loadData(String keyword) {
        tableModel.setRowCount(0);
        List<Student> list = (keyword == null || keyword.isEmpty())
                ? dao.findAll() : dao.findByName(keyword);
        for (Student s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getAge(),
                    s.getGender(), s.getScore(), s.getMajor()
            });
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
        tfMajor.setText(tableModel.getValueAt(row, 5).toString());
    }

    private Student buildFromForm() {
        String name = tfName.getText().trim();
        String gender = tfGender.getText().trim();
        String major = tfMajor.getText().trim();
        if (name.isEmpty()) throw new IllegalArgumentException("姓名不能为空");
        int age = Integer.parseInt(tfAge.getText().trim());
        double score = Double.parseDouble(tfScore.getText().trim());
        return new Student(0, name, age, gender, score, major);
    }

    private void addStudent() {
        try {
            if (dao.add(buildFromForm())) {
                JOptionPane.showMessageDialog(this, "添加成功！");
                loadData(null); clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入有误：" + ex.getMessage());
        }
    }

    private void updateStudent() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先从表格选择要修改的学生"); return;
        }
        try {
            Student s = buildFromForm();
            s.setId(Integer.parseInt(tfId.getText()));
            if (dao.update(s)) {
                JOptionPane.showMessageDialog(this, "修改成功！");
                loadData(null); clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入有误：" + ex.getMessage());
        }
    }

    private void deleteStudent() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先从表格选择要删除的学生"); return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "确认删除该学生？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dao.delete(Integer.parseInt(tfId.getText()));
            JOptionPane.showMessageDialog(this, "删除成功！");
            loadData(null); clearForm();
        }
    }

    private void clearForm() {
        tfId.setText(""); tfName.setText(""); tfAge.setText("");
        tfGender.setText(""); tfScore.setText(""); tfMajor.setText("");
        table.clearSelection();
    }
}