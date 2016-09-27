function syncStoreLoad(storeList, baseParams, onComplete)
{
    if (storeList && storeList.length > 0)
    {
        var currentStore = storeList.shift();

        currentStore.on("load", function(store, records, options)
        {
            syncStoreLoad(storeList, baseParams, onComplete);
            store.un("load");
        });

        currentStore.load(
        {
            params : baseParams
        });
    }
    else
    {
        if (onComplete)
        {
            onComplete();
        }
    }
}

function initCap(str)
{
    return str.substring(0, 1).toUpperCase() + str.substring(1, str.length).toLowerCase();
}

function isEmptyDictionary(dic)
{
    for ( var prop in dic)
    {
        if (dic.hasOwnProperty(prop))
        {
            return false;
        }
    }
    
    return true;
}

function padLeft(text, char, size)
{
    var pattern = char;
    
    for (var i = 1; i <= size; i++)
    {
        pattern = pattern + char;
    }
    
    return (pattern + text).slice(-size);
}
