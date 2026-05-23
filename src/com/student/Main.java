package com.student;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 设置系统外观，解决中文显示问题
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new StudentManagementUI().setVisible(true);
        });
    }
}