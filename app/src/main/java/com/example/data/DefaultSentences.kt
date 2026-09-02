package com.example.data

import com.example.model.Sentence

data class YouTubeLecture(
    val id: String,
    val title: String,
    val channel: String,
    val url: String,
    val description: String
)

object DefaultSentences {
    val YOUTUBE_LECTURES = listOf(
        YouTubeLecture(
            id = "melpark_part3_50",
            title = "파트3 레전드 50 문장! 극 최신기술 반영! 회화까지 OK!",
            channel = "멜 토스 (Melpark)",
            url = "https://www.youtube.com/results?search_query=%EB%A9%9C%ED%86%A0%EC%8A%A4+%ED%8C%8C%ED%8A%B83+%EB%A0%88%EC%A0%84%EB%93%9C+50+%EB%AC%B8%EC%9E%A5",
            description = "토익스피킹 파트3 최신기출 반영 만능 답변 및 실전 영어회화 핵심 50 문장"
        ),
        YouTubeLecture(
            id = "melpark_part5_60",
            title = "스피킹 쉐도윙 만능 60 문장",
            channel = "멜팍 Melpark",
            url = "https://youtu.be/fMMtbCZzjMc?si=ZzYHYEkpdb6Wc56M",
            description = "토익스피킹/오픽/실전 스피킹 파트5 핵심 만능 쉐도윙 문장"
        )
    )

    fun getList(): List<Sentence> = listOf(
        // ==========================================
        // 0-1. YouTube 1: [멜토스] 파트3 레전드 50 문장! 극 최신기술 반영! 회화까지 OK!
        // ==========================================
        Sentence(
            category = "YouTube 강의",
            korean = "집에서 가까워서 가기 편해요.",
            english = "It is close to my house, so it is convenient to go there.",
            acceptableAnswers = "It's close to my house, so it is easy to go there. | It is near my home, so it's very convenient.",
            patternTip = "[YouTube 멜토스 파트3] 'close to my house / near my home' (집 근처) - 장소 선택 이유를 말할 때 1순위 만능 문장입니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "걸어서 5분밖에 안 걸려요.",
            english = "It only takes about five minutes on foot.",
            acceptableAnswers = "It takes only 5 minutes on foot. | It is just a five-minute walk from here.",
            patternTip = "[YouTube 멜토스 파트3] 'It only takes about ~ on foot' (도보로 ~분밖에 안 걸린다) - 접근성 강조 표현.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "가격이 합리적이고 다양한 할인 혜택을 제공해요.",
            english = "The price is reasonable and they offer various discounts.",
            acceptableAnswers = "The prices are reasonable and they give good discounts. | It is reasonably priced and offers many discounts.",
            patternTip = "[YouTube 멜토스 파트3] 'reasonable price'(합리적 가격) + 'offer various discounts'(다양한 할인 제공).",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "선택할 수 있는 옵션이 매우 다양해요.",
            english = "There is a wide variety of options to choose from.",
            acceptableAnswers = "There are a lot of options to choose from. | It provides a wide range of choices.",
            patternTip = "[YouTube 멜토스 파트3] 'a wide variety of options to choose from' - 메뉴, 상품, 취미 선택 시 범용 표현.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "사용 후기와 평점이 좋아서 믿고 이용할 수 있어요.",
            english = "It has good online reviews and high ratings, so it is reliable.",
            acceptableAnswers = "The reviews and ratings are great, so I can trust it. | It has great reviews and ratings, so it's trustworthy.",
            patternTip = "[YouTube 멜토스 파트3] 'good reviews and ratings'(좋은 후기와 평점) + 'reliable / trustworthy'(믿을 만한).",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "스마트폰 앱으로 언제 어디서나 쉽게 주문할 수 있어요.",
            english = "I can easily order using a smartphone app anytime and anywhere.",
            acceptableAnswers = "I can easily make an order with a mobile app anytime, anywhere. | It allows me to order on my smartphone anytime.",
            patternTip = "[YouTube 멜토스 파트3] 'using a smartphone app anytime and anywhere' - 온라인/배달/쇼핑 핵심 문장.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "최신 기술과 트렌드가 잘 반영되어 있어서 아주 유용해요.",
            english = "It reflects the latest technology and trends, which is very useful.",
            acceptableAnswers = "It incorporates the latest technology, so it is very convenient. | It reflects current trends and technology well.",
            patternTip = "[YouTube 멜토스 파트3] 'reflects the latest technology' (최신 기술을 반영하다) - 최신 기출 트렌드 필수 표현.",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "스트레스를 풀고 기분 전환하기에 완벽한 방법이에요.",
            english = "It is a perfect way to relieve stress and refresh my mind.",
            acceptableAnswers = "It's a great way to relieve stress and refresh myself. | It helps me relieve stress and relax.",
            patternTip = "[YouTube 멜토스 파트3] 'relieve stress and refresh my mind' (스트레스 해소와 기분 전환).",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "친구들과 즐겁고 소중한 시간을 보낼 수 있어요.",
            english = "I can spend quality time with my friends.",
            acceptableAnswers = "I can have a great time with my friends. | It lets me spend meaningful time with friends.",
            patternTip = "[YouTube 멜토스 파트3] 'spend quality time with ~' (~와 소중하고 질 높은 시간을 보내다).",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "직원들이 항상 친절하고 서비스가 훌륭해요.",
            english = "The staff are always friendly and the service is great.",
            acceptableAnswers = "The employees are very friendly and the customer service is excellent. | Staff members are always kind and helpful.",
            patternTip = "[YouTube 멜토스 파트3] 'friendly staff + great service' - 매장, 식당, 호텔 관련 답변 1순위 근거.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "분위기가 조용하고 아늑해서 집중하기에 좋아요.",
            english = "The atmosphere is quiet and cozy, so it is great for focusing.",
            acceptableAnswers = "It has a quiet and cozy atmosphere, which helps me focus. | The place is quiet and comfortable to concentrate.",
            patternTip = "[YouTube 멜토스 파트3] 'quiet and cozy atmosphere' (조용하고 아늑한 분위기).",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "인터넷 검색을 통해 최신 정보를 쉽고 빠르게 찾을 수 있어요.",
            english = "I can quickly find up-to-date information through internet search.",
            acceptableAnswers = "I can get the latest information quickly on the internet. | It's easy to find updated information online.",
            patternTip = "[YouTube 멜토스 파트3] 'up-to-date information / latest information' (최신 정보).",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "건강을 유지하고 체력을 기르는 데 큰 도움이 돼요.",
            english = "It helps me stay healthy and build up my stamina.",
            acceptableAnswers = "It is great for staying healthy and building physical strength. | It helps keep me healthy and fit.",
            patternTip = "[YouTube 멜토스 파트3] 'stay healthy and build up stamina' (건강 유지와 체력 증진) - 운동/취미 주제.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "비용 대비 가치(가성비)가 아주 훌륭해요.",
            english = "It offers great value for the money.",
            acceptableAnswers = "It gives excellent value for money. | It is totally worth the price.",
            patternTip = "[YouTube 멜토스 파트3] 'great value for the money' (가성비가 아주 좋다) - 원어민들이 가장 즐겨 쓰는 가성비 표현.",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "다른 사람들의 추천을 받아서 처음 시작하게 되었어요.",
            english = "I first started it based on recommendations from others.",
            acceptableAnswers = "I began doing it because people recommended it. | I started it on a friend's recommendation.",
            patternTip = "[YouTube 멜토스 파트3] 'based on recommendations from others' (타인의 추천을 바탕으로).",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "사용법이 간단하고 직관적이어서 누구나 쉽게 배울 수 있어요.",
            english = "It is simple and intuitive to use, so anyone can learn it easily.",
            acceptableAnswers = "It's easy and intuitive to use, so it's simple to learn. | Because it is user-friendly, anyone can use it easily.",
            patternTip = "[YouTube 멜토스 파트3] 'simple and intuitive to use' (사용이 간단하고 직관적인).",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "바쁜 일상 속에서 소중한 시간을 많이 절약해 줘요.",
            english = "It saves me a lot of precious time in my busy routine.",
            acceptableAnswers = "It saves plenty of valuable time in my busy daily life. | It helps me save precious time.",
            patternTip = "[YouTube 멜토스 파트3] 'save precious time in my busy routine' (바쁜 일상에서 소중한 시간을 아끼다).",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "혼자서도 부담 없이 편안하게 즐길 수 있어요.",
            english = "I can enjoy it comfortably even alone without any pressure.",
            acceptableAnswers = "I can enjoy it by myself comfortably without feeling burdened. | It's easy to enjoy alone without pressure.",
            patternTip = "[YouTube 멜토스 파트3] 'enjoy it comfortably even alone without any pressure' (혼자서도 부담 없이 즐기다).",
            difficulty = "중급"
        ),

        // ==========================================
        // 0-2. YouTube 2: [쉐도윙] 만능 60개 문장 (멜팍 Melpark) (https://youtu.be/fMMtbCZzjMc?si=ZzYHYEkpdb6Wc56M)
        // ==========================================
        Sentence(
            category = "YouTube 강의",
            korean = "그것은 스트레스를 해소하는 데 매우 좋은 방법입니다.",
            english = "It is a great way to relieve stress.",
            acceptableAnswers = "It relieves my stress. | It's a great way to relieve my stress. | That is a very good way to relieve stress.",
            patternTip = "[YouTube 멜팍 만능 60] 'It is a great way to + 동사원형' (~하기에 매우 좋은 방법이다) - 스피킹 이유 설명 시 최고의 만능 도입 구문입니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "그것은 시간과 돈을 많이 절약하는 데 도움이 됩니다.",
            english = "It helps me save a lot of time and money.",
            acceptableAnswers = "It helps to save a lot of time and money. | It can save a lot of time and money.",
            patternTip = "[YouTube 멜팍 만능 60] 'help + 목적어 + 동사원형' 구조: 시간/비용 절약 효과를 설득할 때 사용합니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "언제 어디서나 할 수 있어서 매우 편리합니다.",
            english = "It is very convenient because I can do it anytime, anywhere.",
            acceptableAnswers = "It's so convenient because you can do it anytime, anywhere. | It is very convenient since I can do it anywhere.",
            patternTip = "[YouTube 멜팍 만능 60] 'anytime, anywhere'(언제 어디서나)는 모바일/온라인/자유로운 활동의 장점을 말할 때 핵심입니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "인터넷에서 많은 유용한 정보를 얻을 수 있습니다.",
            english = "I can get a lot of useful information on the Internet.",
            acceptableAnswers = "I can find a lot of useful information on the internet. | You can get lots of useful information online.",
            patternTip = "[YouTube 멜팍 만능 60] information은 셀 수 없는 명사(불가산)이므로 'a lot of information' 형태로 씁니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "새로운 것들을 경험함으로써 시야를 넓힐 수 있습니다.",
            english = "I can broaden my perspective by experiencing new things.",
            acceptableAnswers = "It helps me broaden my perspective. | You can broaden your horizons by experiencing new things.",
            patternTip = "[YouTube 멜팍 만능 60] 'broaden one's perspective / horizons' = 시야와 견문을 넓히다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "그것은 저의 생산성과 업무 효율성을 향상시켜 줍니다.",
            english = "It improves my productivity and work efficiency.",
            acceptableAnswers = "It can improve work efficiency and productivity. | It increases my productivity.",
            patternTip = "[YouTube 멜팍 만능 60] 'productivity'(생산성)와 'work efficiency'(업무 효율)는 직장/학업 스피킹 주제의 필수 어휘입니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "동료들과 돈독하고 좋은 관계를 형성할 수 있습니다.",
            english = "I can build good relationships with my colleagues.",
            acceptableAnswers = "I can build strong relationships with coworkers. | It helps me build good relationships with coworkers.",
            patternTip = "[YouTube 멜팍 만능 60] 'build relationships with ~' = ~와 관계를 형성하다/쌓다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "우선 무엇보다도, 그것은 편안한 분위기를 만들어 줍니다.",
            english = "First of all, it creates a comfortable atmosphere.",
            acceptableAnswers = "First, it creates a cozy atmosphere. | First of all, it makes a comfortable environment.",
            patternTip = "[YouTube 멜팍 만능 60] 'First of all'(우선) + 'create a comfortable atmosphere'(편안한 분위기를 조성하다).",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "사람들이 더 큰 동기부여와 자신감을 갖도록 만들어 줍니다.",
            english = "It makes people feel more motivated and confident.",
            acceptableAnswers = "It helps people feel motivated and confident. | It makes them feel more motivated.",
            patternTip = "[YouTube 멜팍 만능 60] 'make + 목적어 + 형용사/동사원형' 사역 구조로 감정 변화(motivated, confident)를 표현합니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "장기적인 관점에서 볼 때 매우 유익할 것이라 생각합니다.",
            english = "I think it is very beneficial in the long run.",
            acceptableAnswers = "I believe it will be beneficial in the long run. | It is very helpful in the long run.",
            patternTip = "[YouTube 멜팍 만능 60] 'in the long run' = 장기적으로는, 결국에는.",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "팀원들과 직접 얼굴을 마주보고 소통하는 것이 훨씬 쉽습니다.",
            english = "It is much easier to communicate with team members directly.",
            acceptableAnswers = "It's easier to communicate with team members in person. | It is much easier to communicate with colleagues directly.",
            patternTip = "[YouTube 멜팍 만능 60] 'It is much easier to + 동사' 가주어-진주어 비교급 강조 구문입니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "우리는 발생 가능한 잠재적 문제들을 사전에 예방할 수 있습니다.",
            english = "We can prevent potential problems in advance.",
            acceptableAnswers = "We can prevent possible issues in advance. | It allows us to prevent potential problems beforehand.",
            patternTip = "[YouTube 멜팍 만능 60] 'prevent ~ in advance' = 사전에 미리 예방하다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "그것은 고객들에게 매우 긍정적인 인상을 남깁니다.",
            english = "It leaves a good impression on customers.",
            acceptableAnswers = "It gives a positive impression to customers. | It makes a great impression on customers.",
            patternTip = "[YouTube 멜팍 만능 60] 'leave a good impression on ~' = ~에게 좋은 인상을 남기다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "소비자들에게 폭넓고 다양한 선택지를 제공합니다.",
            english = "It provides a wide variety of choices.",
            acceptableAnswers = "It offers a wide range of options. | It gives a wide variety of choices to people.",
            patternTip = "[YouTube 멜팍 만능 60] 'a wide variety of choices/options' = 매우 다양한 선택권.",
            difficulty = "초급"
        ),
        Sentence(
            category = "YouTube 강의",
            korean = "우리는 다른 대안들도 신중하게 검토해 볼 필요가 있습니다.",
            english = "We need to consider other alternatives carefully.",
            acceptableAnswers = "We should consider other options carefully. | We need to think about other alternatives.",
            patternTip = "[YouTube 멜팍 만능 60] 'consider alternatives' = 대안책을 고려하다.",
            difficulty = "중급"
        ),

        // ==========================================
        // 1. 일상 회화 (Daily Conversation)
        // ==========================================
        Sentence(
            category = "일상회화",
            korean = "오늘 퇴근하고 뭐해?",
            english = "What are you doing after work today?",
            acceptableAnswers = "What are you up to after work today? | What will you do after work today?",
            patternTip = "현재진행형(What are you doing)은 이미 예정된 가까운 미래의 일정을 물을 때 자연스럽습니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "일상회화",
            korean = "커피 한잔 사줄게.",
            english = "Let me buy you a cup of coffee.",
            acceptableAnswers = "I'll buy you coffee. | Let me buy you coffee. | Coffee is on me.",
            patternTip = "내가 기꺼이 ~해줄게 할 때는 'Let me + 동사' 또는 'It's on me' 표현을 씁니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "일상회화",
            korean = "주말에 보통 뭐하면서 시간 보내?",
            english = "How do you usually spend your time on weekends?",
            acceptableAnswers = "What do you usually do on weekends? | How do you usually spend your weekends?",
            patternTip = "일상적 습관을 물을 때는 'usually'와 현재 시제를 결합합니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "일상회화",
            korean = "지하철이 너무 붐벼서 늦었어.",
            english = "I was late because the subway was so crowded.",
            acceptableAnswers = "I'm late because the subway was packed. | I got delayed because the subway was too crowded.",
            patternTip = "영어는 원인(because절)보다 결과(I was late)를 먼저 말하는 경향이 강합니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "일상회화",
            korean = "괜찮다면 나중에 전화해 줄래?",
            english = "Could you call me later if you don't mind?",
            acceptableAnswers = "Can you give me a call later if it's okay? | Would you call me back later?",
            patternTip = "'if you don't mind'를 붙이면 훨씬 정중하고 배려 깊은 표현이 됩니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "일상회화",
            korean = "요즘 새로운 운동 시작했어.",
            english = "I started working out recently.",
            acceptableAnswers = "I recently started exercising. | I started a new workout routine lately.",
            patternTip = "운동하다는 'exercise' 외에도 'work out'이 회화에서 매우 빈번하게 사용됩니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "일상회화",
            korean = "오늘 저녁은 집에서 요리해 먹을 거야.",
            english = "I am going to cook dinner at home tonight.",
            acceptableAnswers = "I'm going to make dinner at home tonight. | I will cook at home tonight.",
            patternTip = "개인적인 결심이나 계획은 'be going to + 동사원형'을 사용합니다.",
            difficulty = "초급"
        ),

        // ==========================================
        // 2. 핵심 영어 패턴 (Speaking Patterns)
        // ==========================================
        Sentence(
            category = "패턴영어",
            korean = "~할 계획이야 (나 해외여행 갈 계획이야)",
            english = "I'm planning to travel abroad.",
            acceptableAnswers = "I am planning to go on a trip overseas. | I'm planning to travel overseas.",
            patternTip = "[패턴] I'm planning to + 동사: ~할 계획이나 구상을 가지고 있다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "패턴영어",
            korean = "~하는 게 어때? (잠깐 쉬었다 가는 게 어때?)",
            english = "Why don't we take a short break?",
            acceptableAnswers = "How about taking a short break? | Why don't you take a break?",
            patternTip = "[패턴] Why don't we + 동사: 상대방에게 부드럽게 제안하기.",
            difficulty = "초급"
        ),
        Sentence(
            category = "패턴영어",
            korean = "~하는 걸 고대하고 있어 (너를 직접 만나길 고대하고 있어)",
            english = "I'm looking forward to meeting you in person.",
            acceptableAnswers = "I look forward to seeing you in person. | I am really looking forward to meeting you.",
            patternTip = "[패턴] look forward to + ~ing: to 뒤에 명사 또는 동명사(-ing)가 옵니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "패턴영어",
            korean = "~할 필요 없어 (너무 서두를 필요 없어)",
            english = "You don't need to hurry so much.",
            acceptableAnswers = "You don't have to rush. | There is no need to hurry.",
            patternTip = "[패턴] You don't have to / don't need to: 의무가 없음을 알릴 때 사용.",
            difficulty = "초급"
        ),
        Sentence(
            category = "패턴영어",
            korean = "~해본 적 있어? (혼자 해외여행 가본 적 있어?)",
            english = "Have you ever traveled abroad alone?",
            acceptableAnswers = "Have you ever been abroad alone? | Have you ever traveled overseas by yourself?",
            patternTip = "[패턴] Have you ever + 과거분사(p.p.): 평생 동안의 경험 묻기.",
            difficulty = "중급"
        ),
        Sentence(
            category = "패턴영어",
            korean = "~하는 게 익숙해 (아침 일찍 일어나는 게 익숙해)",
            english = "I'm used to waking up early in the morning.",
            acceptableAnswers = "I am used to getting up early. | I got used to waking up early.",
            patternTip = "[패턴] be used to + -ing: ~에 익숙한 상태를 의미 (used to 동사원형과 구별).",
            difficulty = "중급"
        ),
        Sentence(
            category = "패턴영어",
            korean = "~인 것 같아 (비가 올 것 같아)",
            english = "It looks like it's going to rain.",
            acceptableAnswers = "It seems like it will rain. | It looks like rain.",
            patternTip = "[패턴] It looks like + 절: 눈에 보이는 상황으로 보아 ~인 것 같다.",
            difficulty = "초급"
        ),

        // ==========================================
        // 3. 영어식 사고 훈련 (Thinking in English)
        // ==========================================
        Sentence(
            category = "영어식 사고훈련",
            korean = "그 영화 보고 감동받았어. (그 영화가 나를 감동시켰어)",
            english = "The movie really touched my heart.",
            acceptableAnswers = "I was deeply moved by the movie. | The movie moved me deeply. | The movie was really touching.",
            patternTip = "[영어식 주어] 사물이나 상황을 주어로 삼아 동사(touch, move, inspire)와 함께 표현합니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "영어식 사고훈련",
            korean = "걸어서 거기까지 10분 걸려. (10분 도보 거리야)",
            english = "It takes ten minutes to walk there.",
            acceptableAnswers = "It is a ten minute walk from here. | It takes about 10 minutes on foot.",
            patternTip = "[영어식 거리/시간] 'It takes + 시간 + to 동사' 구조로 생각의 틀을 잡습니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "영어식 사고훈련",
            korean = "어떤 점이 너를 그렇게 생각하게 만들었어? (왜 그렇게 생각해?)",
            english = "What makes you think like that?",
            acceptableAnswers = "What made you think so? | Why do you think that way?",
            patternTip = "[사역 동사 사고] 'Why' 대신 'What makes you...'를 쓰면 훨씬 세련된 영어식 사고가 됩니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "영어식 사고훈련",
            korean = "그 소식 듣고 깜짝 놀랐어.",
            english = "I was surprised to hear the news.",
            acceptableAnswers = "The news surprised me. | I was shocked when I heard the news.",
            patternTip = "감정 형용사(surprised, glad, sad) 뒤에 'to 부정사'를 붙여 감정의 원인을 연결합니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "영어식 사고훈련",
            korean = "너한테 그 일 맡겨도 될까?",
            english = "Can I trust you with this task?",
            acceptableAnswers = "Can I leave this job to you? | Can I count on you for this?",
            patternTip = "'trust someone with something' = ~에게 ~를 믿고 맡기다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "영어식 사고훈련",
            korean = "이 문제에 대해 어떻게 생각해?",
            english = "What do you think about this issue?",
            acceptableAnswers = "What are your thoughts on this matter? | How do you feel about this?",
            patternTip = "한국어는 '어떻게'이지만 영어는 'What'을 써서 의견의 내용을 묻습니다.",
            difficulty = "초급"
        ),

        // ==========================================
        // 4. 비즈니스 & 직장 (Business & Workplace)
        // ==========================================
        Sentence(
            category = "비즈니스",
            korean = "회의 일정을 내일로 변경할 수 있을까요?",
            english = "Could we reschedule the meeting for tomorrow?",
            acceptableAnswers = "Can we move the meeting to tomorrow? | Would it be possible to reschedule the meeting for tomorrow?",
            patternTip = "일정을 변경할 때는 'reschedule' 또는 'move ~ to'를 사용합니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "비즈니스",
            korean = "최신 보고서를 이메일로 첨부했습니다.",
            english = "I have attached the latest report to this email.",
            acceptableAnswers = "Please find attached the latest report. | I attached the updated report.",
            patternTip = "비즈니스 메일에서 첨부 파일 안내 시 'I have attached ~' 또는 'Please find attached ~'.",
            difficulty = "중급"
        ),
        Sentence(
            category = "비즈니스",
            korean = "가능한 한 빨리 검토 후 알려주세요.",
            english = "Please let me know after reviewing it as soon as possible.",
            acceptableAnswers = "Please get back to me as soon as you review it. | Let me know at your earliest convenience.",
            patternTip = "'at your earliest convenience'는 비즈니스에서 정중하게 빠른 피드백을 요청할 때 쓰는 표현입니다.",
            difficulty = "고급"
        ),
        Sentence(
            category = "비즈니스",
            korean = "이번 프로젝트 마감일이 언제인가요?",
            english = "When is the deadline for this project?",
            acceptableAnswers = "What is the deadline for this project? | When is this project due?",
            patternTip = "마감일은 'deadline' 또는 'due date'를 씁니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "비즈니스",
            korean = "그 제안에 대해 전적으로 동의합니다.",
            english = "I completely agree with that proposal.",
            acceptableAnswers = "I totally agree with your proposal. | I am completely on board with that.",
            patternTip = "강한 동의 표현: 'completely agree with' 또는 'on board with'.",
            difficulty = "중급"
        ),

        // ==========================================
        // 5. 여행 & 상황별 (Travel & Situational)
        // ==========================================
        Sentence(
            category = "여행",
            korean = "가장 가까운 지하철역이 어디인지 알려주시겠어요?",
            english = "Could you tell me where the nearest subway station is?",
            acceptableAnswers = "Where is the closest subway station? | Can you show me the way to the nearest subway station?",
            patternTip = "간접의문문 어순: 의문사(where) + 주어(the nearest subway station) + 동사(is).",
            difficulty = "초급"
        ),
        Sentence(
            category = "여행",
            korean = "체크인 시간 전에 짐을 맡길 수 있나요?",
            english = "Can I leave my luggage before check-in?",
            acceptableAnswers = "Could you keep my bags before check-in time? | Can I store my luggage here before check-in?",
            patternTip = "호텔에서 짐 보관 요청 시 'leave/store my luggage' 표현을 사용합니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "여행",
            korean = "계산서 좀 갖다 주시겠어요?",
            english = "Could we have the check, please?",
            acceptableAnswers = "Can I get the bill, please? | Check, please. | Could we get the bill?",
            patternTip = "식당에서 계산을 요청할 때 미국에서는 'the check', 영국 등에서는 'the bill'을 많이 씁니다.",
            difficulty = "초급"
        ),
        Sentence(
            category = "여행",
            korean = "이 근처에 추천해주실 만한 식당이 있나요?",
            english = "Is there any restaurant you would recommend around here?",
            acceptableAnswers = "Can you recommend a good restaurant nearby? | Do you have any restaurant recommendations around here?",
            patternTip = "'recommend' 뒤에 목적어를 놓거나 'recommendations' 명사를 활용합니다.",
            difficulty = "중급"
        ),
        Sentence(
            category = "여행",
            korean = "이 드레스 다른 사이즈로 입어봐도 될까요?",
            english = "Can I try this dress on in a different size?",
            acceptableAnswers = "Could I try this on in another size? | May I try this dress on in a medium size?",
            patternTip = "옷을 입어보다: 'try on'. 크기 변경: 'in a different size'.",
            difficulty = "초급"
        )
    )
}
