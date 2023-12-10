import java.sql.*;
import java.util.Random;

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

                hospitalManager.addBranch("branch # 1");
                hospitalManager.addBranch("branch # 2");

                hospitalManager.addPatient(1, getRandom("name") + " " + getRandom("lastname"), 30, "male");
                hospitalManager.addPatient(1, getRandom("name") + " " + getRandom("lastname"), 30, "female");
                hospitalManager.addPatient(1, getRandom("name") + " " + getRandom("lastname"), 30, "male");
                hospitalManager.addPatient(1, getRandom("name") + " " + getRandom("lastname"), 30, "female");

                hospitalManager.printBranchs();
                hospitalManager.listPatients();

                hospitalManager.editBranch(1, "branch edited");
                hospitalManager.editPatient(1, getRandom("name") + " " + getRandom("lastname"), 40, "Мужской");

                hospitalManager.removeBranch(2);
                hospitalManager.removePatient(2);

                hospitalManager.printBranchs();
                hospitalManager.listPatients();
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
