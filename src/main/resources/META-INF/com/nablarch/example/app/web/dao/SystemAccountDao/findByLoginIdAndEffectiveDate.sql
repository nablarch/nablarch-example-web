select *
from
  system_account
where
  login_id = /*loginId*/'10000001'
  and /* nowDate */'2017-01-01' between effective_date_from and effective_date_to
