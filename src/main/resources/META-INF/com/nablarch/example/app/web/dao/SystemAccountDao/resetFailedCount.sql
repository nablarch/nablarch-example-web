update system_account
set
  /*%if failedCountToLock != 0 */
  failed_count         = 0,
  /*%end*/
  last_login_date_time = /* nowTime */'2017-01-01 01:12:34.123321' 
where user_id = /* userId */107
