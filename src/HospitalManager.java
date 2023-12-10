import java.sql.*;

public class HospitalManager {
    private Connection connection;

    public HospitalManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:hospital2.db");
        } catch (SQLException e) {
            handleException(e);
        }
    }

    public void removeBranch(int branchId) {
        executeUpdate("DELETE FROM Branch WHERE id = ?", branchId);
        System.out.println("Branch deleted: " + branchId);
    }


    public void addBranch(String name) {
        executeUpdate("INSERT INTO Branch (name, patientsCount) VALUES (?, 0)", name);
        System.out.println("Branch added: " + name);
    }




    public void listPatients() {
        try (ResultSet resultSet = executeQuery("SELECT p.id, p.name, p.age, p.sex, d.name AS branch_name " +
                "FROM Patient p INNER JOIN Branch d ON p.branchId = d.id")) {
            System.out.println("Patients list:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String sex = resultSet.getString("sex");
                String branchName = resultSet.getString("branch_name");
                System.out.println("ID: " + id + ", name: " + name + ", age: " + age + ", sex: " + sex + ", Branch: " + branchName);
            }
        } catch (SQLException e) {
            handleException(e);
        }
    }



    public void addPatient(int branchId, String name, int age, String sex) {
        try {
            connection.setAutoCommit(false);

            executeUpdate("INSERT INTO Patient (branchId, name, age, sex) VALUES (?, ?, ?, ?)",
                    branchId, name, age, sex);

            executeUpdate("UPDATE Branch SET patientsCount = patientsCount + 1 WHERE id = ?", branchId);

            connection.commit();
            System.out.println("Patient added: " + name);
        } catch (SQLException e) {
            handleRollbackAndException(e);
        } finally {
            restoreAutoCommit();
        }
    }

    public void removePatient(int patientId) {
        try {
            connection.setAutoCommit(false);

            executeUpdate("DELETE FROM Patient WHERE id = ?", patientId);

            executeUpdate("UPDATE Branch SET patientsCount = patientsCount - 1 WHERE id = (SELECT branchId FROM Patient WHERE id = ?)",
                    patientId);

            connection.commit();
            System.out.println("Patient deleted: " + patientId);
        } catch (SQLException e) {
            handleRollbackAndException(e);
        } finally {
            restoreAutoCommit();
        }
    }

    public void editBranch(int branchId, String newName) {
        executeUpdate("UPDATE Branch SET name = ? WHERE id = ?", newName, branchId);
        System.out.println("Branch information edited: " + branchId);
    }



    public void printBranchs() {
        try (ResultSet resultSet = executeQuery("SELECT id, name, patientsCount FROM Branch")) {
            System.out.println("Branch list:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int patientsCount = resultSet.getInt("patientsCount");
                System.out.println("ID: " + id + ", name: " + name + ", Patients  count: " + patientsCount);
            }
        } catch (SQLException e) {
            handleException(e);
        }
    }

    public void editPatient(int patientId, String newName, int newAge, String newSex) {
        executeUpdate("UPDATE Patient SET name = ?, age = ?, sex = ? WHERE id = ?", newName, newAge, newSex, patientId);
        System.out.println("Patient information edited: " + patientId);
    }

    private void executeUpdate(String sql, Object... parameters) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            setParameters(pstmt, parameters);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        }
    }

    private ResultSet executeQuery(String sql, Object... parameters) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        setParameters(pstmt, parameters);
        return pstmt.executeQuery();
    }

    private void setParameters(PreparedStatement pstmt, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            pstmt.setObject(i + 1, parameters[i]);
        }
    }


    private void restoreAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            handleException(e);
        }
    }



    private void handleException(SQLException e) {
        e.printStackTrace();
    }

    private void handleRollbackAndException(SQLException e) {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            handleException(ex);
        }
        handleException(e);
    }

}
