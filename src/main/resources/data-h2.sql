
insert into tax.store (store_id)
values ('1234567890')
, ('2345678901')
, ('3456789012')
, ('4567890123')
;

insert into tax.transaction_record (transaction_type, amount, store_id, created_at)
values ('REVENUE', 10000, '1234567890', now());

insert into tax.user_info(user_name) values
      ('user-a')
    , ('user-b')
    , ('user-c');

