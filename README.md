**Задача:**

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
(000) 00-00-00

**АЛГОРИТМ**
1. Начало расчета:
2. Получить P_indicators – параметр запуска расчета, массив записей таблицы T_indicators;
3. Получить P_resources – параметр запуска расчета, массив записей таблицы T_resources;
4. Получить P_years – параметра запуска расчета, массив записей таблицы T_years;
5. Для очередного показателя I из P_indicators:
5.1. Для очередного ресурса R из P_resources:
5.1.1. Для очередного года Y из P_years:
5.1.1.1. Получить M_values – массив записей T_values, каждая из которых удовлетворяет условию [[Значение поля «Показатель» = I] и [Значение поля «Ресурс» = R] и [Значение поля «Год» = Y]];
5.1.1.2. Получить M_calculated – массив записей T_multiplication, каждая из которых удовлетворяет условию [[Значение поля «Базовая ЕИ» содержится в поле «Единица измерения» какой-либо записи M_values] и [Значение поля «Расчетная ЕИ» не содержится в поле «Единица измерения» записей M_values]];
5.1.1.3. Получить M_based – массив записей T_multiplication, каждая из которых удовлетворяет условию [[Значение поля «Расчетная ЕИ» содержится в поле «Единица измерения» какой-либо записи M_values] и [Значение поля «Базовая ЕИ» не содержится в поле «Единица измерения» записей M_values]];
5.1.1.4. Получить M_result – массив записей T_convertation, каждая из которых удовлетворяет условию [[Значение поля «Исходная ЕИ» содержится в поле «Единица измерения» какой-либо записи M_values] и [Значение поля «Результирующая ЕИ» не содержится в поле «Единица измерения» записей M_values]];
5.1.1.5. Если [[M_calculated пусто] и [M_based пусто] и [M_result пусто]], то перейти в 5.1.2, иначе перейти в 5.1.1.5.1:
5.1.1.5.1. Если [M_calculated пусто], то перейти в 5.1.1.5.2, иначе перейти в 5.1.1.5.1.1:
5.1.1.5.1.1. Для каждой записи M_values, у которой значение поля «Единица измерения» содержится в поле «Базовая ЕИ» записей M_calculated, рассчитать значения в единицах измерения, которые содержатся в поле «Расчетная ЕИ» соответствующих записей M_calculated по формуле: [Значение в расчетной ЕИ = Значение в базовой ЕИ * 10 E];
5.1.1.5.1.2. Записать в T_values для I, R, Y все значения, рассчитанные в расчетных единицах измерения, перейти в 5.1.1.5.2;
5.1.1.5.2. Если [M_based пусто], то перейти в 5.1.1.5.3, иначе перейти в 5.1.1.5.2.1:
5.1.1.5.2.1. Для каждой записи M_values, у которой значение поля «Единица измерения» содержится в поле «Расчетная ЕИ» записей M_based, рассчитать значения в единицах измерения, которые содержатся в поле «Базовая ЕИ» соответствующих записей M_based по формуле: [Значение в базовой ЕИ = Значение в расчетной ЕИ * 10 –E];
5.1.1.5.2.2. Записать в T_values для I, R, Y все значения, рассчитанные в базовых единицах измерения, перейти в 5.1.1.5.3;
5.1.1.5.3. Если [M_result пусто], то перейти в 5.1.1.1, иначе перейти в 5.1.1.5.3.1:
5.1.1.5.3.1. Для каждой записи M_values, у которой значение поля «Единица измерения» содержится в поле «Исходная ЕИ» записей M_result, рассчитать значения в единицах измерения, которые содержатся в поле «Результирующая
ЕИ» соответствующих записей M_result по формуле: [Значение в результирующей ЕИ = Значение в исходной ЕИ *
K];
5.1.1.5.3.2. Записать в T_values для I, R, Y все значения, рассчитанные в результирующих единицах измерения, перейти в 5.1.1.1;
5.1.2. Если по всем Y из P_years расчет завершен, то перейти в 5.2, иначе перейти в 5.1.1;
5.2. Если по всем R из P_resources расчет завершен, то перейти в 6, иначе перейти в 5.1;
6. Если по всем I из P_indicators расчет завершен, то перейти в 7, иначе перейти в 5;
7. Конец расчета.
Где:
T_indicators – таблица показателей, содержит поле «Наименование».
T_resources – таблица ресурсов, содержит поле «Наименование».
T_years – таблица календарных лет, содержит поле «Наименование».
T_values – таблица значений, содержит поля: «Показатель», «Ресурс», «Год», «Единица измерения», «Значение».
T_multiplication – таблица мультипликации единиц измерения, содержит поля: «Наименование», «Базовая ЕИ», «Расчетная ЕИ», «Степень».
T_convertation – таблица конвертации единиц измерения, содержит поля: «Наименование», «Исходная ЕИ», «Результирующая ЕИ», «Коэффициент».
E – степень мультипликации, поле «Степень» в T_multiplication.
K – коэффициент конвертации, поле «Коэффициент» в T_convertation


**Таблица конвертации единиц измерения**
Наименование   Исходная ЕИ Коэффициент Результирующая ЕИ
Mtce --> Mm3ng        Mtce 751,4768963             Mm3ng
Gft3ng --> Twh      Gft3ng 0,301277062               Twh
MMbtu --> Mj         MMbtu 1055,060005                Mj
Bboe --> Qbtu         Bboe  0,58000001              Qbtu
Gtoe --> Gtce         Gtoe   1,4285714              Gtce
Gj --> Gwh              Gj 0,000277778               Gwh
Ktoe --> Kboe         Ktoe   6,8419054              Kboe
Gm3ng --> Gft3ng     Gm3ng   35,958043            Gft3ng

**Таблица мультипликации единиц измерения**
Наименование      Расчетная ЕИ Степень Базовая ЕИ
Gft3ng <--> ft3ng    Gft3ng         -9      ft3ng
Gtce <--> tce          Gtce         -9        tce
Gtoe <--> toe          Gtoe         -9        toe
MMbtu <--> btu        MMbtu         -6        btu
Mj <--> j                Mj         -6          j
Kboe <--> boe          Kboe         -3        boe
Mtoe <--> toe          Mtoe         -6        toe
Twh <--> wh             Twh        -12         wh
Ktoe <--> toe          Ktoe         -3        toe
Gj <--> j                Gj         -9          j
Mboe <--> boe          Mboe         -6        boe
Mtce <--> tce          Mtce         -6        tce
Gm3ng <--> m3ng       Gm3ng         -9       m3ng
Bboe <--> boe          Bboe         -9        boe
Qbtu <--> btu          Qbtu        -15        btu
Mm3ng <--> m3ng       Mm3ng         -6       m3ng
Mft3ng <--> ft3ng    Mft3ng         -6      ft3ng
Gwh <--> wh             Gwh         -9         wh

**Данные**
Энергетический ресурс: Природный газ (Natural gas)
Единица     Совокупное конечное энергопотребление (Total final consumption)
измерения       2017         2018         2019         2020         2021
---------------------------------------------------------------------------            
Mtoe          148,67       149,33       150,00       150,67       151,33
Gwh       172 903,22   173 670,80   174 450,01   175 229,22   175 996,80
Twh         1 729,03     1 736,71     1 744,50     1 752,29     1 759,97