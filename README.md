# java-kanban
Repository for homework project.
<article class="markdown-body entry-content container-lg" itemprop="text"><h3 tabindex="-1" dir="auto"><a id="user-content-индивидуальная-разработка-проекта---трекер-задач-с-англ-tasktracker" class="anchor" aria-hidden="true" href="#индивидуальная-разработка-проекта---трекер-задач-с-англ-tasktracker"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M7.775 3.275a.75.75 0 001.06 1.06l1.25-1.25a2 2 0 112.83 2.83l-2.5 2.5a2 2 0 01-2.83 0 .75.75 0 00-1.06 1.06 3.5 3.5 0 004.95 0l2.5-2.5a3.5 3.5 0 00-4.95-4.95l-1.25 1.25zm-4.69 9.64a2 2 0 010-2.83l2.5-2.5a2 2 0 012.83 0 .75.75 0 001.06-1.06 3.5 3.5 0 00-4.95 0l-2.5 2.5a3.5 3.5 0 004.95 4.95l1.25-1.25a.75.75 0 00-1.06-1.06l-1.25 1.25a2 2 0 01-2.83 0z"></path></svg></a>Индивидуальная разработка проекта - Трекер задач (с англ. "TaskTracker")</h3>
<details open=""><summary><b>Краткое описание функционала программы</b></summary>
<p dir="auto">Приложение для организации совместной работы над задачами. Программа позволяет выполнять CRUD-операции над задачами.
Сами задачи делятся на разные типы: общие, подзадачи и эпики. Эпики включают в себя подзадачи. Функционал программы
реализован в трех формах:</p>
<ol dir="auto">
<li>сохранение данных в оперативной памяти на локальной машине,</li>
<li>сохранением данных в файл на локальной машине,</li>
<li>сохранение данных в файл на сервере с использованием клиента.</li>
</ol>
</details>
<hr>
<p dir="auto"><g-emoji class="g-emoji" alias="jigsaw" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f9e9.png">🛰</g-emoji> <strong>Стек-технологий и опыт разработки (Java Core)</strong> <g-emoji class="g-emoji" alias="jigsaw" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f9e9.png">🚀</g-emoji></p>
<p dir="auto">В ходе реализации проекта мной были изучен и применен на практике следующий стек технологий:</p>
<details open=""><summary><i>Спринт 03</i></summary>
<ul dir="auto">
<li>Проектирование в стиле ООП:
<ul dir="auto">
<li>методы и классы</li>
<li>инкапсуляция (пакеты, модификаторы доступа, геттеры-сеттеры)</li>
<li>наследование, правило DRY, сокрытие полей, переопределение методов, super, this</li>
<li>IDEA: автогенерация кода, горячие клавиши, плагины, дебаггер</li>
<li>класс Object, метод equals, hashCode, toString</li>
<li>code style</li>
</ul>
</li>
<li>Области видимости переменных</li>
<li>Оператор switch</li>
<li>Git: add, commit (хэш, лог, HEAD), status, .gitkeep, .gitignore, conventional commits, log, reset, diff</li>
<li>MarkDown for ReadMe</li>
</ul>
</details>
<details open=""><summary><i>Спринт 04</i></summary>
<ul dir="auto">
<li>Абстракция и полиморфизм:
<ul dir="auto">
<li>абстрактные класс и метод</li>
<li>интерфейсы</li>
<li>виды полиморфизма: классический, ad-hoc (динамический, статический: перегрузка метода), параметрический</li>
</ul>
</li>
<li>Модификаторы: static, final</li>
<li>Константы</li>
<li>Перечисляемый тип Enum</li>
<li>Приведение типов: явное, скрытое, instanceof</li>
<li>Дженерики, типизированные классы и интерфейсы, границы дженериков</li>
<li>Git: branch, checkout, merge, revert</li>
<li>Утилитарный класс: фабрика</li>
</ul>
</details>
<details open=""><summary><i>Спринт 05</i></summary>
<ul dir="auto">
<li>Алгоритмы:
<ul dir="auto">
<li>асимптотическая сложность</li>
<li>поиск минимума/максимума в массиве (линейный поиск)</li>
<li>бинарный поиск</li>
<li>сортировка (вставками, поразрядная)</li>
</ul>
</li>
<li>Структуры данных:
<ul dir="auto">
<li>Java Collections Framework:
<ul dir="auto">
<li>Iterable, Collection, List, Queue, Set</li>
<li>Map</li>
<li>Comparator, Comparable, сортировка коллекций, String.CASE_INSENSITIVE_ORDER</li>
<li>Collections</li>
</ul>
</li>
</ul>
</li>
</ul>
</details>
<details open=""><summary><i>Спринт 06</i></summary>
<ul dir="auto">
<li>String:
<ul dir="auto">
<li>неизменяемость, пул строк, методы, подстроки, сборка-разборка, форматирование</li>
</ul>
</li>
<li>StringBuilder</li>
<li>Регулярные выражения</li>
<li>Исключения:
<ul dir="auto">
<li>иерархия, стек-трейс, throw, throws</li>
<li>обработка исключений (try-catch-finally)</li>
</ul>
</li>
<li>Работа с файлами:
<ul dir="auto">
<li>File, Files, Path, Paths</li>
<li>Streams: байтовые - InputStream, OutputStream; символьные - Reader, Writer; буферизация</li>
<li>try-with-resources</li>
<li>кодировки, Юникод, Charset, StandardCharsets</li>
</ul>
</li>
<li>Ключевые и зарезервированные слова</li>
</ul>
</details>
