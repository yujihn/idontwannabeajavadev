# Инструкция по использованию программы "FinanceApp"

## 1. Запуск программы
*   **Предварительные условия:** У вас должен быть установлен Java Development Kit (JDK) и Apache Maven.
*   **Инструкция по запуску:**
    1.  Откройте терминал или командную строку.
    2.  Перейдите в папку с проектом (где находится файл `pom.xml`).
    3.  Выполните команду: `mvn clean install && mvn exec:java -Dexec.mainClass="com.financeapp.Main"`
*  После запуска вы увидите меню в консоли.

## 2. Структура программы:

FinanceApp/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── financeapp/
│   │   │           ├── Main.java
│   │   │           ├── models/
│   │   │           │   ├── User.java
│   │   │           │   ├── Wallet.java
│   │   │           │   ├── Transaction.java
│   │   │           │   ├── IncomeCategory.java
│   │   │           │   └── ExpenseCategory.java
│   │   │           ├── services/
│   │   │           │   ├── AuthService.java
│   │   │           │   ├── FinanceService.java
│   │   │           │   └── FileService.java
│   │   │           └── utils/
│   │   │               ├── AuthenticationException.java
│   │   │               ├── BudgetExceededException.java
│   │   │               └── CategoryNotFoundException.java
│   │   └── resources/  (Optional - for external configuration files)
│   └── test/          (Optional - for JUnit tests)
│       └── ...
└── README.md

Программа "FinanceApp" состоит из следующих основных классов и пакетов:

*   `com.financeapp` (пакет):
    *   `Main.java`: Основной класс, содержащий точку входа `main()` и логику управления программой (меню, ввод данных, вызов сервисов).
*   `com.financeapp.models` (пакет):
    *   `User.java`: Класс, представляющий пользователя с именем и паролем.
    *   `Wallet.java`: Класс, представляющий кошелек пользователя, содержащий транзакции, бюджеты и баланс.
    *   `Transaction.java`: Класс, представляющий транзакцию (доход или расход).
    *   `IncomeCategory.java`: Enum, представляющий категории доходов (SALARY, FREELANCE, INVESTMENTS, GIFTS, OTHER, GIFTED).
    *   `ExpenseCategory.java`: Enum, представляющий категории расходов (FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, SHOPPING, HEALTH, OTHER, TRANSFER).
*   `com.financeapp.services` (пакет):
    *   `AuthService.java`: Класс, предоставляющий сервисы для аутентификации пользователей (регистрация, вход, получение пользователя по имени).
    *   `FinanceService.java`: Класс, предоставляющий сервисы для работы с финансами (добавление транзакций, установка бюджета, получение сводки, перевод баланса).
    *   `FileService.java`: Класс, предоставляющий сервисы для работы с файлами (сохранение и загрузка данных пользователей и кошельков).
*   `com.financeapp.utils` (пакет):
    *   `AuthenticationException.java`: Исключение, выбрасываемое при ошибках аутентификации.
    *   `BudgetExceededException.java`: Исключение, выбрасываемое при превышении бюджета.
    *  `CategoryNotFoundException.java`: Исключение, выбрасываемое если категория не найдена.

## 3. Функции программы:

*   **Регистрация (меню 2):**
    *   Позволяет создать нового пользователя в системе.
    *   **Пример:**
        *   `Username:` `newuser`
        *   `Password:` `password123`
        *   После успешной регистрации вы увидите сообщение "Registration successful! Please log in."

*   **Вход (меню 1):**
    *   Позволяет войти в систему, используя зарегистрированные логин и пароль.
    *   **Пример:**
        *   `Username:` `newuser`
        *   `Password:` `password123`
        *   После успешного входа вы увидите приветствие "Login successful! Welcome, newuser"

*   **Главное меню (после входа):**
    *   Позволяет выполнять различные операции по управлению финансами.
    *   **Доступные пункты:**
        1.  **Add income:** Добавление дохода.
        2.  **Add expense:** Добавление расхода.
        3.  **Set budget:** Установка бюджета для категории.
        4.  **Show summary:** Вывод сводной информации о финансах.
        5.  **Calculate by selected categories:** Расчёт доходов или расходов по выбранным категориям.
        6.  **Save and exit:** Сохранение данных и выход.
        7. **Exit without saving:** Выход без сохранения.
   ---
*   **Add income (меню 1):**
    *   Позволяет добавить доход в кошелек пользователя.
    *   **Пример:**
        *   `Enter income category (available categories: SALARY, FREELANCE, INVESTMENTS, GIFTS, OTHER, GIFTED):` `SALARY`
        *   `Enter income amount:` `10000`
        *   После успешного добавления дохода вы увидите сообщение "Income added successfully."

*   **Add expense (меню 2):**
    *   Позволяет добавить расход в кошелек пользователя.
    *   **Пример:**
        *   `Enter expense category (available categories: FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, SHOPPING, HEALTH, OTHER, TRANSFER):` `FOOD`
        *   `Enter expense amount:` `2000`
        *   После успешного добавления расхода вы увидите сообщение "Expense added successfully."
         *    **Пример перевода**:
             *  `Enter expense category (available categories: FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, SHOPPING, HEALTH, OTHER, TRANSFER): TRANSFER`
             *  `Enter user to transfer to:` `marian`
             *  `Enter amount to transfer:` `5000`
             * После успешного перевода вы увидите сообщение "Transfer successful."

*   **Set budget (меню 3):**
    *   Позволяет установить лимит бюджета для определенной категории расходов.
        *  **Пример:**
        *  `Enter budget category (available categories: FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, SHOPPING, HEALTH, OTHER, TRANSFER): FOOD`
         * `Enter budget limit:` `10000`
        *   После успешной установки бюджета вы увидите сообщение "Budget set successfully."
         *  **Пример перевода**:
             * `Enter budget category (available categories: FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, SHOPPING, HEALTH, OTHER, TRANSFER): TRANSFER`
             *  `Enter user to transfer to:` `marian`
             *  `Enter amount to transfer:` `5000`
             * После успешного перевода вы увидите сообщение "Transfer successful."

*   **Show summary (меню 4):**
    *   Показывает сводную информацию о доходах, расходах, бюджетах и балансе пользователя.
    *   **Пример:**
        ```
        --- Summary ---
        Total income: 10000.0
        Incomes by categories:
        - SALARY: 10000.0
        Total expenses: 2000.0
        Budget status:
        - FOOD: Limit: 10000.0, Remaining: 8000.0
        Balance: 8000.0
        ```

*  **Calculate by selected categories (меню 5):**
    * Позволяет рассчитать общую сумму доходов или расходов для выбранных категорий.
     *  **Пример:**
        *   `Enter categories separated by comma (e.g., FOOD,TRANSPORT): FOOD, TRANSPORT`
        *   `Enter transaction type ('income' or 'expense'): expense`
        *   Результат: `Total expense for selected categories: 2000.0`

*   **Save and exit (меню 6):**
    *   Сохраняет все изменения в файлы и завершает работу программы.
*   **Exit without saving (меню 7):**
    *   Завершает работу программы, не сохраняя изменения.

## 4. Примеры использования:
1.  **Создание нового пользователя:** Выберите пункт `2. Register`, введите имя пользователя и пароль.
2.  **Вход в систему:** Выберите пункт `1. Login`, введите имя пользователя и пароль.
3.  **Добавление дохода:** Выберите пункт `1. Add income` и введите категорию и сумму дохода.
4.  **Добавление расхода:** Выберите пункт `2. Add expense` и введите категорию и сумму расхода.
5.  **Установка бюджета:** Выберите пункт `3. Set budget` и введите категорию и лимит бюджета.
6.  **Просмотр сводки:** Выберите пункт `4. Show summary`, чтобы увидеть всю финансовую информацию.
7. **Перевод баланса:** Выберите пункт `3. Set budget`, а затем выберите категорию `TRANSFER`, затем введите имя пользователя и сумму перевода
8. **Расчёт по категориям:** Выберите пункт `5. Calculate by selected categories` выберите категории и тип транзакции.
9.  **Сохранение и выход:** Выберите пункт `6. Save and exit`, чтобы сохранить данные и выйти из программы.

## 5. Завершение работы:
*   После выбора пункта `3. Exit` в главном меню, или `6. Save and exit` в менеджере финансов, программа завершит свою работу.
