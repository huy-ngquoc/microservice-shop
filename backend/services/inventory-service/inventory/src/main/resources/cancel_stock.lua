
local quantity_raw = redis.call('HGET', KEYS[1], 'quantity')
local reserved_raw = redis.call('HGET', KEYS[1], 'reservedQuantity')
local amount = tonumber(ARGV[1])


if not quantity_raw then
    return -1
end


local quantity = tonumber(quantity_raw)
local reserved = tonumber(reserved_raw) or 0 

if quantity == nil then
    return -2 
end


if reserved < amount then
    return 0 
end

redis.call('HINCRBY', KEYS[1], 'quantity', amount)
redis.call('HINCRBY', KEYS[1], 'reservedQuantity', -amount)


return 1