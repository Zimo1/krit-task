import java.util.ArrayList;

// Класс записей для таблицы мультипликации единиц измерения t_multiplication
public class T_multiplication {
    private String multName; // Наименование мультипликатора
    private String baseMeasure; // Базовая единица измерения
    private String calculatedMeasure; // Расчетная единица измерения
    private Integer degree; // Степень преобразования

    // Конструктор
    public T_multiplication(String multName, String baseMeasure, String calculatedMeasure, Integer degree) {
        this.multName = multName;
        this.baseMeasure = baseMeasure;
        this.calculatedMeasure = calculatedMeasure;
        this.degree = degree;
    }

    // Геттеры:
    public String getMultName() {
        return multName;
    }

    public String getBaseMeasure() {
        return baseMeasure;
    }

    public String getCalculatedMeasure() {
        return calculatedMeasure;
    }

    public Integer getDegree() {
        return degree;
    }

    // Метод проверяет выполнение условия [[Значение поля «Базовая ЕИ» содержится в поле «Единица измерения»
    // какой-либо записи M_values] и [Значение поля «Расчетная ЕИ» не содержится в поле
    // «Единица измерения» записей M_values]]
    Boolean baseMesureIncludedInButNeverRepeat(ArrayList<T_values> values) {
        Boolean baseMesureIncludedIn = false;
        Boolean calculatedMesureIncludedIn = false;
        for (T_values value: values) {
            String measure = value.getMeasure();
            if (measure.equals(baseMeasure)) baseMesureIncludedIn = true;
            if (measure.equals(calculatedMeasure)) calculatedMesureIncludedIn = true;
        }
        return baseMesureIncludedIn && !calculatedMesureIncludedIn;
    }

    // Метод проверяет выполнение условия [[Значение поля «Расчетная ЕИ» содержится в поле «Единица измерения»
    // какой-либо записи M_values] и [Значение поля «Базовая ЕИ» не содержится в поле
    // «Единица измерения» записей M_values]];
    Boolean calculatedMesureIncludedInButNeverRepeat(ArrayList<T_values> values) {
        Boolean baseMesureIncludedIn = false;
        Boolean calculatedMesureIncludedIn = false;
        for (T_values value: values) {
            String measure = value.getMeasure();
            if (measure.equals(baseMeasure)) baseMesureIncludedIn = true;
            if (measure.equals(calculatedMeasure)) calculatedMesureIncludedIn = true;
        }
        return calculatedMesureIncludedIn && !baseMesureIncludedIn;
    }
}
