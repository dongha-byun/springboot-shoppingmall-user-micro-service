insert into users(user_name, email, password, login_fail_count, sign_up_date, tel_no, grade, order_count, amount, is_lock, created_at, created_by, updated_at, updated_by)
    values('테스트', 'test@test.com', 'test_password', 0, now(), '010-2345-2345', 'NORMAL', 0, 0, false, now(), 0L, null, null);