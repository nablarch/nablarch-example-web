update system_account
set failed_count = /*failedCount*/2,
  user_id_locked = /*locked*/true
where user_id = /*userId*/106
