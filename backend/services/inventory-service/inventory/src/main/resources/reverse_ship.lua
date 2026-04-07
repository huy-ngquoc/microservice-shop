local quantity_raw = redis.call('HGET', KEYS[1], 'quantity')
local amount = tonumber(ARGV[1])
if not quantity_raw then
    return -1
end

local quantity = tonumber(quantity_raw)

if quantity == nil then
    return -2 
end

redis.call('HINCRBY', KEYS[1], 'quantity', amount)
return 1