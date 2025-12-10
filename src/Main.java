import factory.DAOFactory;
import ui.ConsoleUI;

public class Main {

    public static void main(String[] args) {
        DAOFactory factory = DAOFactory.getFactory(DAOFactory.POSTGRES);
        ConsoleUI ui = new ConsoleUI(factory);
        ui.executar();
    }
}