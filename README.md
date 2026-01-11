
## [TaxController](./src/main/java/com/example/tax/adapter/in/web/TaxController.java) 
* 수집 요청 -> POST: /api/v1/tax  
* 수집 상태 조회 -> GET: /api/v1/tax/{storeId}/state?yearMonth=  
* 부가세 조회 API -> GET:  /api/v1/tax/{storeId}/vat?yearMonth=

## [StoreAccessManagementController](./src/main/java/com/example/tax/adapter/in/web/StoreAccessManagementController.java)
* 권한 부여 -> POST: /api/v1/stores/{storeId}/assignments?userSrl=, X-Admin-Role 헤더: ADMIN 
* 권한 회수 -> DELETE: /api/v1/stores/{storeId}/assignments?userSrl=, X-Admin-Role 헤더: ADMIN
* 권한 조회 -> GET: /api/v1/stores/{storeId}/permissions/me?userSrl=, X-Admin-Role 헤더: ADMIN or MANAGER

## DB' ERD Diagram

[//]: # (<img src="./ERD.md" alt="Description" width="600" height="300">  )
```mermaid
erDiagram
user_info ||--o{ user_store : "1:N"
store ||--o{ user_store : "1:N"
store ||--o{ store_vat : "1:N (논리적 연결)"
store ||--o{ transaction_record : "1:N (논리적 연결)"
store ||--o{ collection_task : "1:N (논리적 연결)"

    user_info {
        bigint srl PK
        varchar user_name
    }

    store {
        bigint srl PK
        varchar store_id "UK, index"
    }

    user_store {
        bigint user_srl PK "FK"
        bigint store_srl PK "FK"
        timestamp created_at
    }

    collection_task {
        bigint srl PK
        varchar store_id "index(1)"
        varchar target_year_month "index(2)"
        varchar status
        timestamp started_at "index(3 DESC)"
        timestamp ended_at
        varchar error_message
    }

    store_vat {
        bigint srl PK
        varchar store_id "index(1)"
        bigint vat "부가세"
        bigint sales "월 매출"
        bigint purchase "월 매입"
        timestamp calculated_at
        varchar target_year_month "index(2)"
    }

    transaction_record {
        bigint srl PK
        varchar transaction_type "index(2)"
        bigint amount
        timestamp created_at
        date transaction_date "index(3)"
        varchar store_id "index(1)"
    }
```
* user_info: 사용자 정보
* store: 상점 정보
* user_store: 사용자-상점 간의 권한 관리를 위한 조인 테이블
* store_vat: 월 단위 매출/매입 합산 금액과 부가세를 저장
* transaction_record: sample.xlsx의 매출/매입 데이터를 저장하는 테이블
* collection_task: transaction_record 테이블에 저장하는 작업 단위의 진행 상태를 저장하는 테이블

