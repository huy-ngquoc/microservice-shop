for i, key in ipairs(KEYS) do
    local quantity_raw = redis.call('HGET', key, 'quantity')
    local reserved_raw = redis.call('HGET', key, 'reservedQuantity')
    local amount = tonumber(ARGV[i])
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
end
for i,key in ipairs(KEYS) do
    local amount = tonumber(ARGV[i])
    redis.call('HINCRBY', key, 'quantity', amount)
    redis.call('HINCRBY', key, 'reservedQuantity', -amount)
end
return 1