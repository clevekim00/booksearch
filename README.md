# booksearch

# Env
## Framework
* spring boot 1.5.7.RELEASE
* jQuery 1.11.2

## DB
* h2

# Restful API
## Search
* /search : 검색

## 이전 검색 결과 조회
* /search/list : 검색 히스토리 리스트 전체 조회
* /search/listpage : 검색 히스토리 리스트 조회 (paging, sort 포함)
* /search/delete : 검색 히스토리 한건 삭제
* /search/delete/all : 검색 히스토리 전체 삭제
* /search/category : category 조회

## 검색조건 히스토리
* /history/list : 검색 조건 히스토리 조회
* /history/delete : 검색 조건 히스토리 한건 삭제
* /history/delete/all : 검색 조건 히스토리 전체 삭제

## 북마크
* /bookmarker : 북마크 추가
* /bookmarker/list : 북마크된 목록 조회
* /bookmarker/delete : 선택된 북마크 삭제
* /bookmarker/delete/all : 전체 북마크 삭제
