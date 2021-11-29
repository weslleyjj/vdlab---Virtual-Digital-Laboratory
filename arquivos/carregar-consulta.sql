select 
medicao0_.id as col_0_0_, medicao0_.id_visita as col_1_0_, visita10_.ordem as col_2_0_, 
visita10_.id_rota as col_3_0_, rota13_.nome as col_4_0_, rota13_.numero_rota as col_5_0_, 
medicao0_.data_cadastro as col_6_0_, medicao0_.data_envio as col_7_0_, medicao0_.data_cadastro_palm as col_8_0_, 
medicao0_.data_atualizacao as col_9_0_, medicao0_.vazao as col_10_0_, medicao0_.temperatura as col_11_0_, 
medicao0_.pressao as col_12_0_, medicao0_.volume_nao_corrigido as col_13_0_, medicao0_.volume_corrigido as col_14_0_, 
medicao0_.consumo_parado as col_15_0_, cliente3_.id as col_16_0_, cliente3_.apelido as col_17_0_, cliente3_.nome as col_18_0_, 
cliente3_.codigo as col_19_0_, medicaoinc2_.id as col_20_0_, cliente3_.horas_consumo as col_21_0_, 
cliente3_.seguimento as col_22_0_, cliente3_.ler_eletrocorretor as col_23_0_, medidor4_.id as col_24_0_, 
gmedidor5_.id as col_25_0_, medidorman6_.id as col_26_0_, fundoescal7_.id as col_27_0_, medidorter8_.id as col_28_0_, 
fundoescal9_.id as col_29_0_, medicaoinc2_.id as id1_17_0_, medidor4_.id as id1_18_1_, gmedidor5_.id as id1_12_2_, 
medidorman6_.id as id1_14_3_, fundoescal7_.id as id1_11_4_, medidorter8_.id as id1_27_5_, fundoescal9_.id as id1_11_6_, 
medicaoinc2_.comentario_inconsistencia as comentar2_17_0_, medicaoinc2_.data_comentario as data_com3_17_0_, 
medicaoinc2_.id_medicao as id_medic7_17_0_, medicaoinc2_.mensagem_inconsistencia as mensagem4_17_0_, 
medicaoinc2_.motivo as motivo5_17_0_, medicaoinc2_.tipo_inconsistencia as tipo_inc6_17_0_, 
medidor4_.id_cliente as id_clie17_18_1_, medidor4_.data_cadastro as data_cad2_18_1_, medidor4_.data_desvinculo as data_des3_18_1_, 
medidor4_.data_exclusao as data_exc4_18_1_, medidor4_.data_fim as data_fim5_18_1_, medidor4_.data_inicio as data_ini6_18_1_, 
medidor4_.data_vinculo as data_vin7_18_1_, medidor4_.id_fabricante as id_fabr18_18_1_, medidor4_.observacao as observac8_18_1_, 
medidor4_.status as status9_18_1_, medidor4_.id_usuario_cadastro as id_usua19_18_1_, medidor4_.cpg as cpg10_18_1_, 
medidor4_.distancia_entre_flanges as distanc11_18_1_, medidor4_.id_dn_medidor as id_dn_m20_18_1_, 
medidor4_.fabricante as fabrica12_18_1_, medidor4_.id_g_medidor as id_g_me21_18_1_, 
medidor4_.id_modelo_medidor as id_mode22_18_1_, medidor4_.num_serie as num_ser13_18_1_, medidor4_.ranges as ranges14_18_1_, 
medidor4_.id_tipo_medidor as id_tipo23_18_1_, medidor4_.valor_fim as valor_f15_18_1_, 
medidor4_.valor_inicio as valor_i16_18_1_, gmedidor5_.nome as nome2_12_2_, gmedidor5_.vazao_maxima as vazao_ma3_12_2_, 
gmedidor5_.vazao_minima as vazao_mi4_12_2_, medidorman6_.id_cliente as id_clie12_14_3_, 
medidorman6_.data_cadastro as data_cad2_14_3_, medidorman6_.data_desvinculo as data_des3_14_3_, 
medidorman6_.data_exclusao as data_exc4_14_3_, medidorman6_.data_fim as data_fim5_14_3_, 
medidorman6_.data_inicio as data_ini6_14_3_, medidorman6_.data_vinculo as data_vin7_14_3_, 
medidorman6_.id_fabricante as id_fabr13_14_3_, medidorman6_.observacao as observac8_14_3_, 
medidorman6_.status as status9_14_3_, medidorman6_.id_usuario_cadastro as id_usua14_14_3_, 
medidorman6_.divisao as divisao10_14_3_, medidorman6_.id_fundo_escala as id_fund15_14_3_, 
medidorman6_.tag as tag11_14_3_, fundoescal7_.fundo_escala as fundo_es2_11_4_, 
fundoescal7_.limite_maximo as limite_m3_11_4_, fundoescal7_.limite_minimo as limite_m4_11_4_, 
medidorter8_.id_cliente as id_clie15_27_5_, medidorter8_.data_cadastro as data_cad2_27_5_, 
medidorter8_.data_desvinculo as data_des3_27_5_, medidorter8_.data_exclusao as data_exc4_27_5_, 
medidorter8_.data_fim as data_fim5_27_5_, medidorter8_.data_inicio as data_ini6_27_5_, 
medidorter8_.data_vinculo as data_vin7_27_5_, medidorter8_.id_fabricante as id_fabr16_27_5_, 
medidorter8_.observacao as observac8_27_5_, medidorter8_.status as status9_27_5_, 
medidorter8_.id_usuario_cadastro as id_usua17_27_5_, medidorter8_.d as d10_27_5_, 
medidorter8_.divisao as divisao11_27_5_, medidorter8_.id_fundo_escala as id_fund18_27_5_,
medidorter8_.haste as haste12_27_5_, medidorter8_.pos as pos13_27_5_, medidorter8_.tag as tag14_27_5_, 
fundoescal9_.fundo_escala as fundo_es2_11_6_, fundoescal9_.limite_maximo as limite_m3_11_6_, 
fundoescal9_.limite_minimo as limite_m4_11_6_ 
from medicao medicao0_ left outer join medicao_inconsistencias inconsiste1_ on medicao0_.id=inconsiste1_.id_medicao 
left outer join medicao_inconsistencias medicaoinc2_ on inconsiste1_.id=medicaoinc2_.id 
inner join cliente cliente3_ on medicao0_.id_cliente=cliente3_.id 
left outer join medidor medidor4_ on cliente3_.id_medidor=medidor4_.id 
left outer join g_medidor gmedidor5_ on medidor4_.id_g_medidor=gmedidor5_.id 
left outer join manometro medidorman6_ on cliente3_.id_medidor_manometro=medidorman6_.id 
left outer join fundo_escala fundoescal7_ on medidorman6_.id_fundo_escala=fundoescal7_.id 
left outer join termometro medidorter8_ on cliente3_.id_medidor_termometro=medidorter8_.id 
left outer join fundo_escala fundoescal9_ on medidorter8_.id_fundo_escala=fundoescal9_.id 
cross join visita visita10_ cross join rota rota13_ 
where medicao0_.id_visita=visita10_.id and visita10_.id_rota=rota13_.id 
and ((medicao0_.data_cadastro between '2005-01-01 00:00:00' and '2021-12-01 23:59:59') 
and (medicao0_.data_cadastro_palm is null) or medicao0_.data_cadastro_palm between '2005-01-01 00:00:00' 
and '2021-12-01 23:59:59') and (medicao0_.ativa=true or medicao0_.ativa is null) 
and (exists (select medicaoinc17_.id from medicao_inconsistencias inconsiste16_, medicao_inconsistencias medicaoinc17_ 
where medicao0_.id=inconsiste16_.id_medicao and inconsiste16_.id=medicaoinc17_.id) or 
medicao0_.id_motivo_impossibilidade is not null) and (medicao0_.id 
in (select distinct medicaoinc18_.id_medicao from medicao_inconsistencias medicaoinc18_ 
where medicaoinc18_.tipo_inconsistencia in ('VOLUME_EQUIPAMENTO' , 'MANOMETRO' , 'TERMOMETRO'))) 
and (medicao0_.id_cliente in (select distinct visita19_.id_cliente from visita visita19_ 
where (visita19_.id_rota in (1,2,3,4,5,6,7,8,9,10,11,12)) and visita19_.status=100)) 
order by cliente3_.nome, coalesce(medicao0_.data_cadastro_palm, medicao0_.data_cadastro) desc limit 10