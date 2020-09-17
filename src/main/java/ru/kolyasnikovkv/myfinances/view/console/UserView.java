package ru.kolyasnikovkv.myfinances.view.console;

import org.apache.commons.codec.digest.DigestUtils;
import ru.kolyasnikovkv.myfinances.dao.DaoFactory;
import ru.kolyasnikovkv.myfinances.service.PersonService;
import ru.kolyasnikovkv.myfinances.service.ServiceFactory;
import ru.kolyasnikovkv.myfinances.service.dto.UserDto;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserView {
    Scanner scanner = new Scanner(System.in);
    private final PersonService personService = ServiceFactory.getPersonService();

    private String toMD5(String start) {
        return DigestUtils.md5Hex(start);
    }


    public UserDto createNewPerson(UserDto userDto) throws SQLException {

        if (userDto != null) {

            userDto = personService.createNewPerson(userDto, DaoFactory.getConnection());

            if (userDto != null) {
                System.out.println("New userDto " + userDto.getMail() + " successfully created!");
                return userDto;
            } else {
                System.out.println("Возможно данный e-mail " + userDto.getMail() + " уже зарегистрирован!" +
                        "\n Повторная регистрация запрещена!");
                return null;
            }
        }
        System.out.println("Что-то пошло не так! ");
        return null;
    }

    public UserDto createUserDto() {
        System.out.println("Please print your e-mail and press <Enter>");
        String mail = scanner.nextLine().trim();
        UserDto userDto = new UserDto();

        while (true) {
            if (validateMail(mail)) {
                System.out.println("Please print your password and press <Enter>");
                userDto.setMail(mail);
                String password = scanner.nextLine().trim();

                while (true) {
                    if (validatePassword(password)) {
                        System.out.println("Please print your Nick name and press <Enter>");
                        userDto.setPassword(toMD5(password));
                        String nick = scanner.nextLine().trim();

                        while (true) {

                            if (validateNickName(nick)) {
                                System.out.println("Please print your full name and press <Enter>");
                                userDto.setNick(nick);
                                String fullName = scanner.nextLine().trim();

                                while (true) {
                                    if (validateFullName(fullName)) {
                                        userDto.setFullName(fullName);
                                        return userDto;
                                    }
                                    else {
                                        System.out.println("for exit press q or Q");
                                        fullName = scanner.nextLine().trim();

                                        if (fullName.equalsIgnoreCase("q")) {
                                            return null;
                                        }
                                    }
                                }

                            }
                            else {
                                System.out.println("for exit press q or Q");
                                nick = scanner.nextLine().trim();

                                if (nick.equalsIgnoreCase("q")) {
                                    return null;
                                }

                            }
                        }
                    }
                    else {
                        System.out.println("Our password is not valid. Please try again");
                        System.out.println("for exit press q or Q");
                        password = scanner.nextLine().trim();

                        if (password.equalsIgnoreCase("q")) {
                            return null;
                        }
                    }
                }

            } else {
                System.out.println("Our e-mail is not valid. Please try again");
                System.out.println("for exit press q or Q");
                mail = scanner.nextLine().trim();

                if (mail.equalsIgnoreCase("q")) {
                    return null;
                }
            }
        }
    }

    private boolean validateMail(String mail) {

        if (mail.length() > 60) {
            return false;
        }
        Pattern p = Pattern.compile("[a-zA-Z0-9\\-_]+@[a-zA-Z]+\\.[a-zA-Z]{2,4}");
        Matcher m = p.matcher(mail);
        return m.matches();
    }

    private boolean validatePassword(String password) {

        if (password.length() < 5) {
            System.out.println("minimum password length = 6, maximum = 15!");
            return false;
        }

        Pattern p = Pattern.compile("[a-zA-Z0-9\\-\\.\\+\\*\\/!@#$%\\^\\&\\(\\)\\{\\}\\~_\\?\"\':;,]{6,15}");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    private boolean validateNickName(String nick) {

        if (nick != null && nick.length() > 15) {
            System.out.println("Your nickname is not valid, maximum length nickName = 15!");
            return false;
        }

        return true;
    }

    private boolean validateFullName(String fullName) {

        if (fullName != null && fullName.length() > 60) {
            System.out.println("There are too many letters. Try again.");
            return false;
        }

        return true;
    }

    UserDto isExistUser() throws SQLException {
        UserDto userDto = getMailTalking();

        if (userDto != null) {

            userDto = personService.authorize(userDto, DaoFactory.getConnection());
            if (userDto != null) {
                System.out.println("Successfully! ");
                return userDto;
            }
        }
        return null;
    }

    private UserDto getMailTalking() {
        System.out.println("Please print your e-mail and press <Enter>");
        String mail = scanner.nextLine().trim();

        if (validateMail(mail)) {
            System.out.println("Please print your password and press <Enter>");
            String password = scanner.nextLine().trim();

            if (validatePassword(password)) {
                UserDto user = new UserDto();
                user.setMail(mail);
                user.setPassword(toMD5(password));
                return user;
            }
            else {
                System.out.println("Your password is not validate!");
                return null;
            }
        }
        else {
            System.out.println("e-mail is not correct!");
        }
        return null;
    }


}