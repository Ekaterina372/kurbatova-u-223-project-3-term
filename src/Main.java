import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final String[] RANDOM_NAMES = {"Бенедикт", "Беломор", "Бекмамбет", "Базилик", "Бенефис", "Будапешт", "Бугимэн", "Бадминтон"};
    private static final String[] RANDOM_LASTNAMES = {"Камбербэтч", "Казантип", "Кабачок", "Курткобейн", "Камамбер", "Драмэндбэйс", "Купидон", "Карабас"};

    private static String getRandom(String something) {
        Random random = new Random();
        int randomIndex;
        switch (something) {
            case "name":
                randomIndex = random.nextInt(RANDOM_NAMES.length);
                return RANDOM_NAMES[randomIndex];
            case "lastname":
                randomIndex = random.nextInt(RANDOM_LASTNAMES.length);
                return RANDOM_LASTNAMES[randomIndex];
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:Hospital.db")) {
            if (connection != null) {
                System.out.println("SQLite connection - feels good man!");
                HospitalManager hospitalManager = new HospitalManager();
                Scanner scanner = new Scanner(System.in);

                int choice;
                do {
                    System.out.println("Выберите действие:");
                    System.out.println("1. Добавить отделение");
                    System.out.println("2. Добавить пациента");
                    System.out.println("3. Вывести отделения");
                    System.out.println("4. Вывести пациентов");
                    System.out.println("5. Редактировать отделение");
                    System.out.println("6. Редактировать пациента");
                    System.out.println("7. Удалить отделение");
                    System.out.println("8. Удалить пациента");
                    System.out.println("0. Выйти");

                    System.out.print("Ваш выбор: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Чтобы считать символ новой строки после ввода числа

                    switch (choice) {
                        case 1:
                            System.out.print("Введите название отделения: ");
                            String branchName = scanner.nextLine();
                            hospitalManager.addBranch(branchName);
                            break;
                        case 2:
                            System.out.print("Введите номер отделения: ");
                            int branchNumber = scanner.nextInt();
                            scanner.nextLine(); // Очистить символ новой строки после ввода числа

                            System.out.print("Введите данные пациента: ");
                            String patientData = scanner.nextLine();

                            System.out.print("Введите возраст пациента: ");
                            int patientAge = scanner.nextInt();

                            System.out.print("Введите пол пациента: ");
                            String patientGender = scanner.next();

                            hospitalManager.addPatient(branchNumber, patientData, patientAge, patientGender);
                            break;

                        case 3:
                            hospitalManager.printBranchs();
                            break;
                        case 4:
                            hospitalManager.listPatients();
                            break;
                        case 5:
                            System.out.print("Введите номер отделения для редактирования: ");
                            int editBranchNumber = scanner.nextInt();
                            System.out.print("Введите новое название отделения: ");
                            String newBranchName = scanner.next();
                            hospitalManager.editBranch(editBranchNumber, newBranchName);
                            break;
                        case 6:
                            System.out.print("Введите номер пациента для редактирования: ");
                            int editPatientNumber = scanner.nextInt();
                            scanner.nextLine(); // Очистить символ новой строки после ввода числа
                            System.out.print("Введите данные пациента: ");
                            String newPatientData = scanner.nextLine();
                            System.out.print("Введите новый возраст пациента: ");
                            int newPatientAge = scanner.nextInt();
                            System.out.print("Введите новый пол пациента: ");
                            String newPatientGender = scanner.next();
                            hospitalManager.editPatient(editPatientNumber, newPatientData, newPatientAge, newPatientGender);
                            break;
                        case 7:
                            System.out.print("Введите номер отделения для удаления: ");
                            int removeBranchNumber = scanner.nextInt();
                            hospitalManager.removeBranch(removeBranchNumber);
                            break;
                        case 8:
                            System.out.print("Введите номер пациента для удаления: ");
                            int removePatientNumber = scanner.nextInt();
                            hospitalManager.removePatient(removePatientNumber);
                            break;
                        case 0:
                            System.out.println("Программа завершена.");
                            break;
                        default:
                            System.out.println("Некорректный выбор. Попробуйте снова.");
                    }
                } while (choice != 0);
            } else {
                System.out.println("SQLite connection - feels bad man(");
            }
        } catch (SQLException e) {
            handleException(e);
        }
    }

    private static void handleException(SQLException e) {
        System.err.println("SQLite database error!");
        e.printStackTrace();
    }
}
