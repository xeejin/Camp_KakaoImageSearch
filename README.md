# :iphone: Kakao Image Search

<br/>

<br/>

## :tada: Used Tech
- Kotlin Language
- UI Layout
- Fragment
- RecyclerView, Adapter
- SharedPreference
- OpenAPI, Retrofit, JSON
<br/>

## :hammer: 기본


- [ ]  이미지를 검색해서 보관함에 수집하는 안드로이드 앱을 구현
- [ ]  키워드를 입력하고 검색을 누르면 이미지 검색 결과 리스트를 보여주도록 구현
- [ ]  이미지 검색 API는 ([**링크**](https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-image))의 thumbnail_url 필드를 사용
- [ ]  UI는 fragment 2개를 사용 (버튼이나 탭 선택 시 전환)


<br/>
 
## :hammer: MainActivity

- [ ]  Fragment를 보여줄 FrameLayout을 만들고, 하단에 2개의 버튼(이미지 검색, 내보관함) 을 생성
- [ ]  MainActivity 시작시 이미지 검색 fragment를 초기화면에 설정

<br/>

## :hammer: 이미지 검색 Fragment

- [ ]  검색어를 입력할 수 있도록 검색창을 구현
- [ ]  검색어를 입력하고 검색 버튼을 누르면 검색된 이미지 리스트를 보여줌
- [ ]  검색 버튼을 누르면 키보드는 숨김 처리하도록 구현
- [ ]  API 검색 결과에서 thumbnail_url, display_sitename, datetime을 받아오도록 구현
- [ ]  RecyclerView의 각 아이템 레이아웃을 썸네일 이미지, 사이트이름, 날짜 시간 으로 구현 
- [ ]  API 검색 결과를 RecyclerView에 표시하도록 구현
- [ ]  날짜 시간은 "yyyy-MM-dd HH:mm:ss” 포멧으로 노출되도록 구현
- [ ]  검색 결과는 최대 80개까지만 표시하도록 구현
- [ ]  리스트에서 특정 이미지를 선택하면 **특별한 표시**를 보여주도록 구현 (좋아요/별표/하트 등)
- [ ]  선택된 이미지는 MainActivity의 ‘선택된 이미지 리스트 변수’에 저장
- [ ]  마지막 검색어는 저장 되며, 앱 재시작시 마지막 검색어가 검색창 입력 필드에 자동으로 입력

<br/>

## :hammer: 내 보관함 Fragment

- [ ]  MainActivity의 ‘선택된 이미지 리스트 변수’에서 데이터를 받아오도록 구현
- [ ]  내보관함 Recyclerview는 ‘이미지는 검색’ 과 동일하게 구현
- [ ]  내보관함에 보관된 이미지를 선택하면 보관함에서 제거할 수 있도록 구현

<br/>


