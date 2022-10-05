package com.andreikslpv.filmfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.model.Film
import com.andreikslpv.filmfinder.presentation.adRecycler.AdRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.itemDecoration.TopSpacingItemDecoration
import com.andreikslpv.filmfinder.presentation.filmListRecycler.FilmListRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.touchHelper.FilmTouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var posterCount: Int = 0
    val filmsDataBase = listOf(
        Film(
            "Во все тяжкие",
            R.drawable.poster_1,
            "Умирающий учитель химии начинает варить мет ради благополучия семьи. Выдающийся драматический сериал 2010-х",
            "Школьный учитель химии Уолтер Уайт узнаёт, что болен раком лёгких. Учитывая сложное финансовое состояние дел семьи, а также перспективы, Уолтер решает заняться изготовлением метамфетамина. Для этого он привлекает своего бывшего ученика Джесси Пинкмана, когда-то исключённого из школы при активном содействии Уайта. Пинкман сам занимался варкой мета, но накануне, в ходе рейда УБН, он лишился подельника и лаборатории."
        ),
        Film(
            "Декстер",
            R.drawable.poster_2,
            "Судмедэксперт в свободное время убивает преступников. Детектив, который заставляет сопереживать маньяку",
            "Я — Декстер. Декстер Морган. Я работаю судмедэкспертом в полиции Майами. Я не понимаю любви, мне безразличен секс, и у меня нет чувств. А еще я серийный убийца.\n" +
                    "Мой отец был полицейским и прекрасно обучил меня прятать улики. Обычным гражданам не стоит меня бояться, я убиваю только преступников, подонков, до которых почему-либо не смогла или не захотела добраться полиция. Я убиваю их, аккуратно распиливаю на части и избавляюсь от трупов. Убийство для меня — творческий акт, искусство.\n" +
                    "Но однажды… В Майами появляется некто, равный мне. Даже превзошедший меня. Сумевший меня заинтересовать. Предложивший мне своего рода товарищеское состязание. Кто этот загадочный и неуловимый «икс»?.."
        ),
        Film(
            "Футурама",
            R.drawable.poster_3,
            "Курьер-лузер переносится в XXXI век и учится жить в мире космолетов и роботов. Сатира от авторов «Симпсонов»",
            "По нелепой случайности Фрай попадает в будущее — в 3000 год, где его ждут верные друзья в лице безумного робота Бендера и одноглазой Лилы."
        ),
        Film(
            "Доктор Хаус",
            R.drawable.poster_4,
            "Все люди врут, но этого не проведешь. Выдающийся медицинский детектив о докторе-гении",
            "Сериал рассказывает о команде врачей, которые должны правильно поставить диагноз пациенту и спасти его. Возглавляет команду доктор Грегори Хаус, который ходит с тростью после того, как его мышечный инфаркт в правой ноге слишком поздно правильно диагностировали. Как врач Хаус просто гений, но сам не отличается проникновенностью в общении с больными и с удовольствием избегает их, если только есть возможность. Он сам всё время проводит в борьбе с собственной болью, а трость в его руке только подчеркивает его жесткую, ядовитую манеру общения. Порой его поведение можно назвать почти бесчеловечным, и при этом он прекрасный врач, обладающий нетипичным умом и безупречным инстинктом, что снискало ему глубокое уважение. Будучи инфекционистом, он ещё и замечательный диагност, который любит разгадывать медицинские загадки, чтобы спасти кому-то жизнь. Если бы все было по его воле, то Хаус лечил бы больных не выходя из своего кабинета."
        ),
        Film(
            "Убийство",
            R.drawable.poster_5,
            "История о расследовании убийства с разных точек зрения. Атмосферный триллер, основанный на датском сериале",
            "Одно убийство с трех точек зрения — детективов, семьи погибшей и подозреваемых. Следствие затрагивает местных политиков и их связь с этим делом. Постепенно становится ясно, что здесь нет случайностей, у каждого свой секрет."
        ),
        Film(
            "Мост",
            R.drawable.poster_6,
            "Преступник оставляет труп ровно на границе между Данией и Швецией. Образцовый скандинавский детектив",
            "На самой середине Эресуннского моста, связывающего Швецию с Данией, происходит краткое отключение электроэнергии. Появление освещения сопровождается обнаружением незнакомого объекта: тела женщины, обращенного головой к Швеции, ногами — к Дании. Чья полиция должна руководить расследованием убийства подброшенной за несколько минут мертвой женщины?"
        ),
        Film(
            "Рик и Морти",
            R.drawable.poster_7,
            "Гениальный ученый втягивает внука в безумные авантюры. Выдающийся анимационный сериал Дэна Хармона",
            "В центре сюжета - школьник по имени Морти и его дедушка Рик. Морти - самый обычный мальчик, который ничем не отличается от своих сверстников. А вот его дедуля занимается необычными научными исследованиями и зачастую полностью неадекватен. Он может в любое время дня и ночи схватить внука и отправиться вместе с ним в безумные приключения с помощью построенной из разного хлама летающей тарелки, которая способна перемещаться сквозь межпространственный тоннель. Каждый раз эта парочка оказывается в самых неожиданных местах и самых нелепых ситуациях."
        ),
        Film(
            "Настоящий детектив",
            R.drawable.poster_8,
            "Кто стоит за необычайно жестокими и запутанными убийствами? Суперзвезды в главном детективном сериале 2010-х",
            "Первый сезон. В Луизиане в 1995 году происходит странное убийство девушки. В 2012 году дело об убийстве 1995 года повторно открывают, так как произошло похожее убийство. Чтобы продвинуться в расследовании, полиция решает допросить бывших детективов, которые работали над тем делом.\n" +
                    "Второй сезон. В калифорнийском городе Винчи в преддверии презентации новой линии железной дороги, которая улучшит финансовое положение города, пропадает глава администрации города. Позже его труп находят на шоссе. К расследованию подключают детектива из полиции Винчи и детектива из департамента шерифа округа Вентура.\n" +
                    "Третий сезон. Известняковое плато Озарк, расположенное одновременно в нескольких штатах. Детектив Уэйн Хейз совместно со следователем из Арканзаса Роландом Уэстом пытаются разобраться в загадочном преступлении, растянувшемся на три десятилетия."
        ),
    )
    private lateinit var filmsAdapter: FilmListRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.ad_recycler)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = AdRecyclerAdapter(getImagesIdentifiers())

        initMainRecycler()
        initMenus()
    }

    private fun initMainRecycler() {
        val filmListRecycler = findViewById<RecyclerView>(R.id.film_list_recycler)
        filmListRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        //Создаем бандл и кладем туда объект с данными фильма
                        val bundle = Bundle()
                        //Первым параметром указывается ключ, по которому потом будем искать, вторым сам
                        //передаваемый объект
                        bundle.putParcelable("film", film)
                        //Запускаем наше активити
                        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                        //Прикрепляем бандл к интенту
                        intent.putExtras(bundle)
                        //Запускаем активити через интент
                        startActivity(intent)
                    }
                })
            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutManager
            layoutManager = LinearLayoutManager(this@MainActivity)
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
            val callback = FilmTouchHelperCallback(adapter as FilmListRecyclerAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)
        }
        //Кладем нашу БД в RV
        filmsAdapter.changeItems(filmsDataBase)
    }

    private fun getImagesIdentifiers(): ArrayList<Int> {
        var resID: Int
        var imageNumber = 1
        val images = ArrayList<Int>()
        do {
            resID = resources.getIdentifier(
                "poster_$imageNumber",
                "drawable",
                packageName
            )
            if (resID != 0) images.add(resID)
            imageNumber++
        } while (resID != 0)
        posterCount = images.size
        return images
    }

    private fun changePosterAndToast(text: CharSequence) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        val recyclerView: RecyclerView = findViewById(R.id.ad_recycler)
        var i = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(2))
        i++
        if (i >= posterCount) i = 0
        recyclerView.scrollToPosition(i)
    }

    private fun initMenus() {
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    changePosterAndToast(it.title!!)
                    true
                }
                else -> false
            }
        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites, R.id.watch_later, R.id.selections -> {
                    changePosterAndToast(it.title!!)
                    true
                }
                else -> false
            }
        }
    }
}