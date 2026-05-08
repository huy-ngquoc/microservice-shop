for i, key in ipairs(KEYS) do
    local quantity_raw = redis.call('HGET', key, 'quantity')
    local amount = tonumber(ARGV[i])
    if not quantity_raw then 
        return -1
    end
    local quantity= tonumber(quantity_raw)
    if quantity==nil then
        return -2
    end
end
for i, key in ipairs(KEYS) do
    local amount = tonumber(ARGV[i])
    redis.call('HINCRBY', key, 'quantity', amount)
end
return 1