/* ОПТИМИЗИРОВАННЫЙ ВАРИАНТ (Изменен первоначальный алгоритм)
Суслопаров Алексей Владимирович. Ижевск, 2020
ТЕСТОВАЯ ЗАДАЧА
        Запрос от Министерства энергетики Нской области:
        Добрый день!
        Нужно срочно решить проблему с пересчетом показателей!
        Делали все по инструкции:
        Заполнили обе таблицы перевода единиц измерения (см. вложения "Таблица конвертации ЕИ", "Таблица мультипликации ЕИ").
        В таблицу значений внесли данные по совокупному конечному энергопотреблению природного газа с 2017 по 2021 годы в единице измерения Mtoe.
        Запустили расчет.
        Данные рассчитались некорректно – значения в Twh отличаются от Gwh на 2 порядка (см. вложение "Данные TFC"), а не на 3, как должно быть. При этом в таблице перевода цифры правильные – степень Gwh -9, степень Twh -12, разница -3. В таблице "Данные TFC" для наглядности оставил только исходные значения в Mtoe и значения в Gwh и Twh, рассчитанные некорректно. В значениях, рассчитанных в остальных единицах измерения, ошибок не нашел, поэтому убрал их из таблицы "Данные TFC".
        Прошу срочно исправить алгоритм расчета (см. вложение "Алгоритм расчета ЕИ").
        Также прошу максимально оптимизировать алгоритм расчета с целью повышения быстродействия.
        --
        В.В. Васильев
        Ведущий специалист отдела перспективного развития
        Министерства энергетики Нской области
        (000) 00-00-00*/

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class SolutionAdvanced {
    // Задать значения таблиц в соответствии с Приложениями к задаче:
    // Параметры запуска расчета:
    private static String p_indicators[] = {"Совокупное конечное энергопотребление"};
    private static String p_resources[] = {"Природный газ"};
    private static String p_years[] = {"2017", "2018", "2019", "2020", "2021"};
    // Таблица значений t_values:
    private static ArrayList<T_values> t_values = new ArrayList<>(Arrays.asList(
            new T_values("Совокупное конечное энергопотребление", "Природный газ", "2017", "Mtoe", 148.67),
            new T_values("Совокупное конечное энергопотребление", "Природный газ", "2018", "Mtoe", 149.33),
            new T_values("Совокупное конечное энергопотребление", "Природный газ", "2019", "Mtoe", 150.00),
            new T_values("Совокупное конечное энергопотребление", "Природный газ", "2020", "Mtoe", 150.67),
            new T_values("Совокупное конечное энергопотребление", "Природный газ", "2021", "Mtoe", 151.33)));
    // Таблица мультипликации единиц измерения t_multiplication:
    private static ArrayList<T_multiplication> t_multiplication = new ArrayList<>(Arrays.asList(
            new T_multiplication("Gft3ng<-->ft3ng", "ft3ng", "Gft3ng", -9),
            new T_multiplication("Gtce<-->tce", "tce", "Gtce", -9),
            new T_multiplication("Gtoe<-->toe", "toe", "Gtoe", -9),
            new T_multiplication("MMbtu<-->btu", "btu", "MMbtu", -6),
            new T_multiplication("Mj<-->j", "j", "Mj", -6),
            new T_multiplication("Kboe<-->boe", "boe", "Kboe", -3),
            new T_multiplication("Mtoe<-->toe", "toe", "Mtoe", -6),
            new T_multiplication("Twh<-->wh", "wh", "Twh", -12),
            new T_multiplication("Ktoe<-->toe", "toe", "Ktoe", -3),
            new T_multiplication("Gj<-->j", "j", "Gj", -9),
            new T_multiplication("Mboe<-->boe", "boe", "Mboe", -6),
            new T_multiplication("Mtce<-->tce", "tce", "Mtce", -6),
            new T_multiplication("Gm3ng<-->m3ng", "m3ng", "Gm3ng", -9),
            new T_multiplication("Bboe<-->boe", "boe", "Bboe", -9),
            new T_multiplication("Qbtu<-->btu", "btu", "Qbtu", -15),
            new T_multiplication("Mm3ng<-->m3ng", "m3ng", "Mm3ng", -6),
            new T_multiplication("Mft3ng<-->ft3ng", "ft3ng", "Mft3ng", -6),
            new T_multiplication("Gwh<-->wh", "wh", "Gwh", -9)));
    // Таблица конвертации единиц измерения t_convertation:
    // Первоначальная версия таблицы конвертации (сейчас отключена):
//    private static ArrayList<T_convertation> t_convertation = new ArrayList<>(Arrays.asList(
//            new T_convertation("Mtce --> Mm3ng", "Mtce", "Mm3ng", 751.4768963),
//            new T_convertation("Gft3ng --> Twh", "Gft3ng", "Twh", 0.301277062),
//            new T_convertation("MMbtu --> Mj", "MMbtu", "Mj", 1055.060005),
//            new T_convertation("Bboe --> Qbtu", "Bboe", "Qbtu", 5.8000001),
//            new T_convertation("Gtoe --> Gtce", "Gtoe", "Gtce", 1.4285714),
//            new T_convertation("Gj --> Gwh", "Gj", "Gwh", 0.000277778),
//            new T_convertation("Ktoe --> Kboe", "Ktoe", "Kboe", 6.8419054),
//            new T_convertation("Gm3ng --> Gft3ng", "Gm3ng", "Gft3ng", 35.958043)));
    // Исправленная версия таблицы конвертации (действующая):
    private static ArrayList<T_convertation> t_convertation = new ArrayList<>(Arrays.asList(
            new T_convertation("Mtce --> Mm3ng", "Mtce", "Mm3ng", 751.4768963),
            new T_convertation("Gft3ng --> Twh", "Gft3ng", "Twh", 0.301277062),
            new T_convertation("Mj --> MMbtu", "Mj", "MMbtu", 0.000947813),
            new T_convertation("Qbtu --> Bboe", "Qbtu", "Bboe", 0.17241379),
            new T_convertation("Gtoe --> Gtce", "Gtoe", "Gtce", 1.4285714),
            new T_convertation("Gwh --> Gj", "Gwh", "Gj", 3599.99712),
            new T_convertation("Kboe --> Ktoe", "Kboe", "Ktoe", 0.146158116),
            new T_convertation("Gm3ng --> Gft3ng", "Gm3ng", "Gft3ng", 35.958043)));
    // Рабочие таблицы:
    private static ArrayList<T_values> m_values = new ArrayList<>();
    private static ArrayList<T_multiplication> m_calculated = new ArrayList<>();
    private static ArrayList<T_multiplication> m_based = new ArrayList<>();
    private static ArrayList<T_convertation> m_result = new ArrayList<>();
    private static ArrayList<T_values> d_values = new ArrayList<>(); // добавлен мной - временный буфер для новых значений

    // ГЛАВНЫЙ МЕТОД
    public static void main(String[] args) {
        System.out.println("Рассчет начат...");
        // Засечь время работы программы
        Instant timerStart = Instant.now();
        // 5. Для очередного показателя I из P_indicators:
        for (String i: p_indicators) {
            System.out.println("Показатель: " + i);
            // 5.1. Для очередного ресурса R из P_resources:
            for (String r: p_resources) {
                System.out.println("Ресурс: " + r);
                // 5.1.1. Для очередного года Y из P_years:
                for (String y: p_years) {
                    System.out.println("Год: " + y);
                    Integer iterationCounter = 0;
                    // 5.1.1.1. Получить M_values – массив записей T_values, каждая из которых удовлетворяет
                    // условию [[Значение поля «Показатель» = I] и [Значение поля «Ресурс» = R] и [Значение поля «Год» = Y]];
                    m_values.clear();
                    for (T_values t_value : t_values) {
                        if (t_value.match(i, r, y)) {
                            m_values.add(t_value);
                            System.out.println("В m_values добавлен элемент: "
                                    + t_value.getMeasure() + " " + t_value.getValue());
                        }
                    }
                    while (true) {
                        // 5.1.1.2. Получить M_calculated – массив записей T_multiplication, каждая из которых
                        // удовлетворяет условию [[Значение поля «Базовая ЕИ» содержится в поле «Единица измерения»
                        // какой-либо записи M_values] и [Значение поля «Расчетная ЕИ» не содержится в поле
                        // «Единица измерения» записей M_values]];
                        iterationCounter++;
                        System.out.println("=== итерация: " + iterationCounter);
                        m_calculated.clear();
                        for (T_multiplication t_multiplicat : t_multiplication) {
                            if (t_multiplicat.baseMesureIncludedInButNeverRepeat(m_values)) {
                                m_calculated.add(t_multiplicat);
                                System.out.println("В m_calculated добавлен мультипликатор: " + t_multiplicat.getMultName());
                            }
                        }
                        // 5.1.1.3. Получить M_based – массив записей T_multiplication, каждая из которых удовлетворяет
                        // условию [[Значение поля «Расчетная ЕИ» содержится в поле «Единица измерения» какой-либо записи
                        // M_values] и [Значение поля «Базовая ЕИ» не содержится в поле «Единица измерения» записей M_values]];
                        m_based.clear();
                        for (T_multiplication t_multiplicat : t_multiplication) {
                            if (t_multiplicat.calculatedMesureIncludedInButNeverRepeat(m_values)) {
                                m_based.add(t_multiplicat);
                                System.out.println("В m_based добавлен мультипликатор: " + t_multiplicat.getMultName());
                            }
                        }
                        // 5.1.1.4. Получить M_result – массив записей T_convertation, каждая из которых удовлетворяет
                        // условию [[Значение поля «Исходная ЕИ» содержится в поле «Единица измерения» какой-либо
                        // записи M_values] и [Значение поля «Результирующая ЕИ» не содержится в поле «Единица измерения»
                        // записей M_values]];
                        m_result.clear();
                        for (T_convertation t_convert : t_convertation) {
                            if (t_convert.sourceMesureIncludedInButNeverRepeat(m_values)) {
                                m_result.add(t_convert);
                                System.out.println("В m_result добавлено правило: " + t_convert.getConvertName());
                            }
                        }
                        // 5.1.1.5. Если [[M_calculated пусто] и [M_based пусто] и [M_result пусто]], то перейти в 5.1.2,
                        // иначе перейти в 5.1.1.5.1:
                        if (m_calculated.isEmpty() && m_based.isEmpty() && m_result.isEmpty()) break; // Единственный выход из цикла while (true)
                        // 5.1.1.5.1. Если [M_calculated пусто], то перейти в 5.1.1.5.2, иначе перейти в 5.1.1.5.1.1:
                        if (!m_calculated.isEmpty()) {
                            // 5.1.1.5.1.1. Для каждой записи M_values, у которой значение поля «Единица измерения» содержится
                            // в поле «Базовая ЕИ» записей M_calculated, рассчитать значения в единицах измерения, которые
                            // содержатся в поле «Расчетная ЕИ» соответствующих записей M_calculated по формуле:
                            // [Значение в расчетной ЕИ = Значение в базовой ЕИ * 10 E];
                            for (T_values m_value : m_values) {
                                for (T_multiplication calculated : m_calculated) {
                                    if (m_value.measureIncludedInBaseMeasure(calculated)) {
                                        Double result = m_value.getValue() * Math.pow(10, calculated.getDegree());
                                        // 5.1.1.5.1.2. Записать в d_values для I, R, Y  значения, рассчитанные в
                                        // расчетных единицах измерения, перейти в 5.1.1.5.2;
                                        T_values newELement = new T_values(i, r, y, calculated.getCalculatedMeasure(), result);
                                        d_values.add(newELement);
                                        System.out.println("В d_values добавлен элемент: "
                                                + newELement.getMeasure() + " " + newELement.getValue());
                                    }
                                }
                            }
                        }
                        // 5.1.1.5.2. Если [M_based пусто], то перейти в 5.1.1.5.3, иначе перейти в 5.1.1.5.2.1:
                        if (!m_based.isEmpty()) {
                            // 5.1.1.5.2.1. Для каждой записи M_values, у которой значение поля «Единица измерения» содержится
                            // в поле «Расчетная ЕИ» записей M_based, рассчитать значения в единицах измерения, которые
                            // содержатся в поле «Базовая ЕИ» соответствующих записей M_based по формуле:
                            // [Значение в базовой ЕИ = Значение в расчетной ЕИ * 10 –E];
                            for (T_values m_value : m_values) {
                                for (T_multiplication based : m_based) {
                                    if (m_value.measureIncludedInCalculatedMeasure(based)) {
                                        Double result = m_value.getValue() * Math.pow(10, -1 * based.getDegree());
                                        // 5.1.1.5.2.2. Записать в d_values для I, R, Y значения, рассчитанные в
                                        // базовых единицах измерения, перейти в 5.1.1.5.3;
                                        T_values newELement = new T_values(i, r, y, based.getBaseMeasure(), result);
                                        d_values.add(newELement);
                                        System.out.println("В d_values добавлен элемент: "
                                                + newELement.getMeasure() + " " + newELement.getValue());
                                    }
                                }
                            }
                        }
                        // 5.1.1.5.3. Если [M_result пусто], то перейти в 5.1.1.2, иначе перейти в 5.1.1.5.3.1:
                        if (!m_result.isEmpty()) {
                            // 5.1.1.5.3.1. Для каждой записи M_values, у которой значение поля «Единица измерения» содержится
                            // в поле «Исходная ЕИ» записей M_result, рассчитать значения в единицах измерения, которые
                            // содержатся в поле «Результирующая ЕИ» соответствующих записей M_result по формуле:
                            // [Значение в результирующей ЕИ = Значение в исходной ЕИ * K];
                            for (T_values m_value : m_values) {
                                for (T_convertation convertation : m_result) {
                                    if (m_value.measureIncludedInSourceMesure(convertation)) {
                                        Double result = m_value.getValue() * convertation.getCoefficient();
                                        // 5.1.1.5.3.2. Записать в d_values для I, R, Y  значения, рассчитанные в
                                        // результирующих единицах измерения, перейти в 5.1.1.2;
                                        T_values newElement = new T_values(i, r, y, convertation.getResultMesure(), result);
                                        d_values.add(newElement);
                                        System.out.println("В d_values добавлен элемент: "
                                                + newElement.getMeasure() + " " + newElement.getValue());
                                    }
                                }
                            }
                        }
                        // Переписать в m_values все полученные значения из временного буфера d_values
                        m_values.addAll(d_values);
                        // Очистить буфер
                        d_values.clear();
                        // перейти в 5.1.1.2;
                    } // while(true)...
                    // Записать все значения из m_values в t_values, кроме первого (оно уже есть в t_values)
                    for (int x = 1; x < m_values.size(); x++) {
                        T_values newElement = m_values.get(x);
                        t_values.add(newElement);
                        System.out.println("В t_values добавлен элемент: "
                                + newElement.getMeasure() + " " + newElement.getValue());
                    }
                // 5.1.2. Если по всем Y из P_years расчет завершен, то перейти в 5.2, иначе перейти в 5.1.1;
                }
            // 5.2. Если по всем R из P_resources расчет завершен, то перейти в 6, иначе перейти в 5.1;
            }
        // 6. Если по всем I из P_indicators расчет завершен, то перейти в 7, иначе перейти в 5;
        } // 7. Конец расчета.

        // ВЫВОД РЕЗУЛЬТАТОВ РАБОТЫ ПРОГРАММЫ:
        System.out.println();
        System.out.println("ИТОГОВАЯ ТАБЛИЦА ПОКАЗАТЕЛЕЙ");
        System.out.println("Показатель: " + p_indicators[0]);
        System.out.println("Ресурс: " + p_resources[0]);
        System.out.println("Количество строк таблицы: " + m_values.size());
        printTable(m_values.size(), p_years.length);
        Instant timerFinish = Instant.now();
        System.out.println();
        System.out.println("Рассчет окончен. Продолжительность: "
                + Duration.between(timerStart, timerFinish).toMillis() + " мсек.");
    } // КОНЕЦ ГЛАВНОГО МЕТОДА

    // Вспомогательная процедура: вывод итоговой таблицы на экран в читабельном виде
    static void printTable(Integer tableRows, Integer tableColumns) {
        String header = "      ";
        String line = String.format("%-6s", t_values.get(0).getMeasure());
        for (int j = 0; j < tableColumns; j++) {
            header = header + String.format("%24s", p_years[j]);
            line = line + String.format("%24s", t_values.get(j).getValue());
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(header);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(line);
        tableRows--;
        for (int i = 0; i < tableRows; i++) {
            line = String.format("%-6s", t_values.get(i + tableColumns).getMeasure());
            for (int j = 0; j < tableColumns; j++) {
                line = line + String.format("%24s", t_values.get(i + j * tableRows + tableColumns).getValue());
            }
            System.out.println(line);
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
    }
}
