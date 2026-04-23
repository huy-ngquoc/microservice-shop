
for i, key in ipairs(KEYS) do
    local stock = redis.call('HGET', key, 'quantity')
    if not stock or tonumber(stock) < tonumber(ARGV[i]) then
        return 0 
    end
end


for i, key in ipairs(KEYS) do
    redis.call('HINCRBY', key, 'quantity', -tonumber(ARGV[i]))
    redis.call('HINCRBY', key, 'reservedQuantity', tonumber(ARGV[i]))
end

return 1 