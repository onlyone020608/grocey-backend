-- 유저 더미 데이터
INSERT INTO user (user_name, email, password, age_group, gender) VALUES ('TestUser', 'onlyone@naver.com', '1234!', 'TWENTIES', 'FEMALE');

-- 냉장고 더미
INSERT INTO fridge (fridge_temperature, freezer_temperature) VALUES (4.0, -18.0);

UPDATE user SET fridge_id = 1 WHERE id = 1;
