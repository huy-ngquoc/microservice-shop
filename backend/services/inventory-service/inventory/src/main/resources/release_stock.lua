local reserved_raw = redis.call('HGET', KEYS[1], 'reservedQuantity')
local amount = tonumber(ARGV[1])
if not reserved_raw then
    return -1
end
local reserved = tonumber(reserved_raw) or 0

if reserved < amount then
    return 0 
end
redis.call('HINCRBY', KEYS[1], 'reservedQuantity', amount)
return 1
