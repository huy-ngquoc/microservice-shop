for i, key in ipairs(KEYS) do
    local reserved_raw = redis.call('HGET', key, 'status')
    local status = ARGV[i]
    redis.call('HSET',key , 'status', status)
return 1