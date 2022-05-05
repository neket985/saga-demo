## Правила формирования повествования (саги)

1) Шаг повествования может быть, либо компенсируемым, либо повторяемым
2) Компенсируемый шаг не может идти после повторяемого

[//]: # (todo может и нет)

3) Каждый шаг должен быть идемпотентным
4) Для борьбы с "аномалиями" из-за недостаточной изолированности надо применять дополнительные меры. например использовать большее число шагов и состояний (в том числе _pending) для уточнения, что
   транзакция еще не завершилась и данные "грязные"

## Варианты отказа

1) логическая ошибка во внешнем сервисе
2) обрыв соединения с внешним сервисом (работу он мог выполнить)
3) ошибка инициации саги в бд
4) ошибка аппрува шага саги в бд
5) резкая остановка оркестратора (сага не завершилась до конца)

## Полезные ссылки

* [хабр](https://habr.com/ru/company/ozontech/blog/590709/)
* [статья `Semantic ACID Properties in Multidatabases Using Remote Procedure Calls and Update Propagations`](https://dl.acm.org/doi/10.5555/284472.284478)
* 