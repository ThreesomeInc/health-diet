--remote: https://diet.martinho0330.com/h2/login.jsp
--local: https://localhost:8081/h2/login.jsp
--username/password see application-dev.yml
create table basic_body_info (
  id int(20) primary key auto_increment,
  open_id varchar(255),
  temp_id varchar(255),
  gender varchar(20),
  birth varchar(20),
  height varchar(20),
  weight varchar(20),
  sport_rate varchar(255),
  nephrotic_period varchar(255),
  treatment_method varchar(255),
  other_disease varchar(255),
  irritability varchar(255),
  create_user varchar(255),
  update_user varchar(255),
  update_time varchar(255),
  create_time varchar(255),
  comment varchar(255)
);