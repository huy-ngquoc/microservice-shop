local reserved_raw = redis.call('HGET', KEYS[1], 'status')
local status = ARGV[1]
local key = KEYS[1]
redis.call('HSET',key , 'status', status)
return 1