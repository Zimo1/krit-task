import java.util.ArrayList;

// Класс записей для таблицы конвертации единиц измерения t_convertation
public class T_convertation {
    private String convertName; // Наименование правила конвертации
    private String sourceMesure; // Исходная единица измерения
    private String resultMesure; // Результирующая единица измерения
    private Double coefficient; //Коэффициент преобразования

    // Конструктор
    public T_convertation(String convertName, String sourceMesure, String resultMesure, Double coefficient) {
        this.convertName = convertName;
        this.sourceMesure = sourceMesure;
        this.resultMesure = resultMesure;
        this.coefficient = coefficient;
    }

    // Геттеры
    public String getConvertName() {
        return convertName;
    }

    public String getSourceMesure() {
        return sourceMesure;
    }

    public String getResultMesure() {
        return resultMesure;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    // Метод проверяет выполнение условия [[Значение поля «Исходная ЕИ» содержится в поле «Единица измерения» какой-либо
    // записи M_values] и [Значение поля «Результирующая ЕИ» не содержится в поле «Единица измерения» записей M_values]]
    Boolean sourceMesureIncludedInButNeverRepeat(ArrayList<T_values> values) {
        Boolean sourceMesureIncludedIn = false;
        Boolean resultMesureIncludedIn = false;
        for (T_values value: values) {
            String measure = value.getMeasure();
            if (measure.equals(sourceMesure)) sourceMesureIncludedIn = true;
            if (measure.equals(resultMesure)) resultMesureIncludedIn = true;
        }
        return sourceMesureIncludedIn && !resultMesureIncludedIn;
    }
}
