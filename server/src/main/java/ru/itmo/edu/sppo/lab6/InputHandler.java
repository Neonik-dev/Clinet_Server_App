package ru.itmo.edu.sppo.lab6;

import ru.itmo.edu.sppo.lab6.command.BaseCommand;
import ru.itmo.edu.sppo.lab6.command.Commands;
import ru.itmo.edu.sppo.lab6.command.SaveCommand;
import ru.itmo.edu.sppo.lab6.dto.ClientRequest;
import ru.itmo.edu.sppo.lab6.dto.ClientResponse;
import ru.itmo.edu.sppo.lab6.exceptions.IncorrectDataEntryExceptions;
import ru.itmo.edu.sppo.lab6.exceptions.UnexpectedCommandExceptions;
import ru.itmo.edu.sppo.lab6.storage.GetServerCommands;
import ru.itmo.edu.sppo.lab6.storage.HistoryStorage;
import ru.itmo.edu.sppo.lab6.utils.Printer;

import java.util.Map;

public class InputHandler {
    public static final String GET_ALL_COMMANDS = "getAllCommands";
    private static final String RECOMMENDATION_HELP_COMMAND =
            "Напишите любую команду из списка. Чтобы посмотреть список команд воспользуйтесь командой -> help";
    private static final Map<String, BaseCommand> COMMANDS = new Commands().getAllCommand();
    private static final BaseCommand SAVE_COMMAND = new SaveCommand();

    private InputHandler() {
    }

    public static Object executeCommand(ClientRequest request) {
        Printer printer = new Printer();
        try {
            BaseCommand command = COMMANDS.get(request.getCommandName());
            if (request.getCommandName().equals(GET_ALL_COMMANDS)) {
                return GetServerCommands.getDetails();
            } else if (command == null) {
                throw new NullPointerException();
            } else {
                HistoryStorage.add(request.getCommandName());
                ClientResponse response = command.execute(request, printer);
                SAVE_COMMAND.execute(request, printer);
                return response;
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            printer.println(RECOMMENDATION_HELP_COMMAND);
        } catch (IncorrectDataEntryExceptions | UnexpectedCommandExceptions e) {
            printer.println(e.getMessage());
        }
        return new ClientResponse(printer.toString());
    }
}
