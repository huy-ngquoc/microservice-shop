for i, key in ipairs(KEYS) do
    local reserved_raw = redis.call('HGET', key, 'reservedQuantity')
    local amount = tonumber(ARGV[i])
    if not reserved_raw then
        return -1
    end
    local reserved = tonumber(reserved_raw) or 0

    if reserved < amount then
        return 0 
    end
end
for i, key in ipairs(KEYS) do
    local amount = tonumber(ARGV[i])
    redis.call('HINCRBY', key, 'reservedQuantity', -amount)
end
return 1