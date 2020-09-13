// Класс записей для таблицы значений показателей t_values
public class T_values {
    private String indicator; // Показатель
    private String resource; // Ресурс
    private String year; // Год
    private String measure; // Единица измерения
    private Double value; // Значение показателя

    // Конструктор
    public T_values(String indicator, String resource, String year, String measure, Double value) {
        this.indicator = indicator;
        this.resource = resource;
        this.year = year;
        this.measure = measure;
        this.value = value;
    }

    // Геттеры:
    public String getMeasure() {
        return measure;
    }

    public Double getValue() {
        return value;
    }

    // Метод для поиска записей T_values, совпадающих по значению индикатора, ресурса и года
    public Boolean match(String indicator, String resource, String year) {
        if ((this.indicator.equalsIgnoreCase(indicator)) &&
            (this.resource.equalsIgnoreCase(resource)) &&
            (this.year.equalsIgnoreCase(year))) return true;
        else return false;
    }

    // Метод проверяет, совпадает ли единица измерения текущей записи T_values (значение поля "measure")
    // с базовой единицей измерения переданной записи таблицы мультипликации T_multiplication (значением поля "baseMeasure")
    Boolean measureIncludedInBaseMeasure(T_multiplication calculated) {
        if (calculated.getBaseMeasure().equals(measure)) return true;
        else return false;
    }

    // Метод проверяет, совпадает ли единица измерения текущей записи T_values (значение поля "measure")
    // с рассчетной единицей измерения переданной записи таблицы мультипликации T_multiplication (значением поля "calculatedMeasure")
    Boolean measureIncludedInCalculatedMeasure(T_multiplication based) {
        if (based.getCalculatedMeasure().equals(measure)) return true;
        else return false;
    }

    // Метод проверяет, совпадает ли единица измерения текущей записи T_values (значение поля "measure")
    // с исходной единицей измерения переданной записи таблицы конвертации T_convertation (значением поля "sourceMesure")
    Boolean measureIncludedInSourceMesure(T_convertation convertation) {
        if (convertation.getSourceMesure().equals(measure)) return true;
        else return false;
    }
}
