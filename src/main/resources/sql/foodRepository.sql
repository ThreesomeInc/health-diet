
create table test.food_repository
(
  id int auto_increment
    primary key,
  food_code varchar(20) null,
  food_group varchar(255) null,
  sub_code varchar(20) null,
  sub_group varchar(255) null,
  code varchar(20) null,
  food_name varchar(255) null,
  edible varchar(10) null comment '食部',
  water varchar(10) null,
  energy_kcal varchar(10) null,
  energy_kj varchar(10) null,
  protein varchar(10) null,
  protein_weight int null,
  fat varchar(10) null,
  fat_weight int null,
  cho varchar(10) null comment '碳水化合物',
  glisemik_index varchar(10) null comment '血糖指数',
  cho_weight int null,
  dietary_fiber varchar(10) default '0' null COMMENT '不溶性纤维',
  cholesterol varchar(10) null comment '胆固醇',
  cholesterol_weight int null,
  ash varchar(10) null COMMENT '灰分',
  purine varchar(10) null COMMENT '嘌呤',
  purine_weight int null,
  vit_a_total varchar(10) null,
  carotene varchar(10) null comment '胡萝卜素',
  retinol varchar(10) null comment '视黄醇',
  thiamin varchar(10) null comment '硫胺素',
  riboflavin varchar(10) null comment '核黄素',
  niacin varchar(10) null comment '尼克酸',
  vit_c varchar(10) null,
  vit_e_total varchar(10) null,
  vit_e_alpha varchar(10) null,
  vit_e_beta_garma varchar(10) null,
  vit_e_delta varchar(10) null,
  Ca varchar(10) null,
  P varchar(10) null comment '磷',
  K varchar(10) null comment 'K/钾',
  Na varchar(10) null comment '钠',
  Mg varchar(10) null,
  Fe varchar(10) null,
  Zn varchar(10) null,
  Se varchar(10) null,
  Cu varchar(10) null,
  Mn varchar(10) null,
  I varchar(10) null,
  total_cellulose varchar(10) null comment '总纤维素',
  soluble varchar(10) null comment '可溶性纤维',
  vit_b_6 varchar(10) null,
  vit_b_12 varchar(10) null,
  folate varchar(10) null comment '叶酸',
  niacin_1 varchar(10) null,
  version varchar(10) null,
  glisemik_index_1 varchar(10) null,
  remark varchar(255) null,
  protein_devide_p varchar(10) null
);

create table test.food_element_evaluation(
   id int auto_increment primary key,
   food_code int;
   protein_evaluation int;
   fat_evalaution int;
   cho_evaluation int;
   cholesterol_evaluation int;
   purine_evaluation int;
   k_evaluation int;
   na_evaluation int;
   p_evaluation int;
)

alter table test.food_element_evaluation add constraint 'food_evaluation_fk'
foreign key ('food_code') references 'food_repository'('code') on delete cascade on update cascade;