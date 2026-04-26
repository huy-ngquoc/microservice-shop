for i, key in ipairs(KEYS) do
    local amount = ARGV[i]
    redis.call('HINCRBY', key, 'reservedQuantity', amount)
end
return 1