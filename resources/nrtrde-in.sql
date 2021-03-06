-- NRTRDE IN Data Gathering Scripts
-- inf_subscriber_all.dun_flag  1: normal, 2: VIP1, 3: VIP3

-- to get the msisdn,dunning flag and the subscriber status of the given IMSI
select a.imsi,
       a.msisdn,
       decode(a.dun_flag,'1','normal','2','VIP1','3','VIP3') sub_type,
       decode(a.sub_state,'B01','ACTIVE','B02','DEACTIVE','B03','SUSPEND','BARRING','B05','PENDING','DEFAULT','UNKNOWN') sub_state       
from inf_subscriber_all a 
where 
     a.msisdn='07704615305' and
     a.prepaid_flag='1';

select * from inf_status a;

-- to get the roaming activation date of the given IMSI
select * from inf_products t where t.sub_id='907701105701' and t.create_date > sysdate -1;
select * from pdm_prodtab t where lower(t.prod_name) like '%start%roaming%';
select * from pdm_prodtab t where lower(t.prod_name) like '%roam%';
select * from his_inf_orderinfo t where t.sub_id='907701105701';
select * from inf_sub_roam t where t.msisdn='07701105701' and rownum < 2 order by t.busi_seq desc;
select busi_seq from (select * from inf_products t where t.sub_id='907701105701' and t.product_id='S90001' order by t.create_date desc) where rownum < 2;
-- to get the last roaming activation/deactivation record for a given MSISDN
select * from inf_sub_roam b where b.busi_seq=(select busi_seq from (select * from inf_products t where t.sub_id='907701105701' and t.product_id='S90001' order by t.create_date desc) where rownum < 2);

-- to get the deposit of the given IMSI
select sum(t.invoice_amt) from billing.receivables t where t.msisdn='07704615305' and t.trans_type='DEP' and t.crdr='DR';
-- to get the total outstanding amount for the given subscriber msisdn/imsi
select sum(t.open_amt) from billing.receivables t where t.msisdn='07704615305' and t.trans_type='BLL' and t.crdr='DR';
-- to get the credit cutoff limit for the given MSISDN
select t.original_credit - t.bar_threshold from dc_init_credit t where t.group_id=(select a.group_id from dc_acct_ccg a where a.acct_id=(select acct_id from inf_acct b where b.cust_id=(select cust_id from inf_subscriber_all c where c.msisdn='07704615305')));

-- or use the following table which contain all of the amounts
select * from SYSDAT_ACCT01 t where t.acct_id=(select a.acct_id from inf_acct a where a.cust_id=(select cust_id from inf_subscriber_all b where msisdn='07701105323') and a.acct_code like '1.1%');
-- test queries
select * from billing.receivables t where t.open_amt > 0;
select * from billing.receivables t where t.msisdn='07704925000';
Select distinct Cust_Id, Sub_Id, Acct_Id, Msisdn,                       
            Sum(Decode(Trans_Type, 'DEP', Abs(Open_Amt), 0)) Deposit,
            Sum(Decode(Trans_Type, 'BLL', Invoice_Amt, 0)) Tot_Bil_Amt,
            Sum(Decode(Trans_Type, 'BLL', Open_Amt, 0)) Tot_Os_Amt
            From billing.Receivables
            Where Trans_Type In ('BLL', 'DEP')
            Group By Cust_Id, Sub_Id, Acct_Id, Msisdn;
select * from inf_acct t where t.cust_id=(select h.cust_id from inf_subscriber_all h where h.msisdn='07704925000');            
select * from inf_customer_all;
Select * From SYSDAT_ACCT01;
Select * From dc_acct_ccg;
Select * From dc_init_credit;



