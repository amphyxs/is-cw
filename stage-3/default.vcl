vcl 4.0;

backend default {
    .host = "localhost";  
    .port = "18124";
}

# Hash function is essential to include query parameters in the cache key.
sub vcl_hash {
    if (req.url ~ "^/shop") {
        hash_data(req.url);
        # Include query parameters gameName and genres if present
        if (req.url ~ "gameName=") {
            hash_data(regsub(req.url, "^.*gameName=([^&]+).*$", "\1"));
        }
        if (req.url ~ "genres=") {
            hash_data(regsub(req.url, "^.*genres=([^&]+).*$", "\1"));
        }
    }
}

# Called at the beginning of a request, after a complete request has been received and parsed.
sub vcl_recv {
    # Normalize the request method to improve cache hit rate
    if (req.method == "GET" || req.method == "HEAD") {
        set req.method = "GET";
    } else {
        # Pass all other requests directly to the backend
        return (pass);
    }

    # Only cache GET and HEAD requests
    if (req.method != "GET" && req.method != "HEAD") {
        return (pass);
    }

    if (req.url ~ "^/game" || req.url ~ "^/shop") {
        # Cache requests to these endpoints
        return (hash);
    }

    # Do not cache other requests by default
    return (pass);
}

# Called after a document has been successfully retrieved from the backend.
sub vcl_backend_response {
    # Cache the response for the /game endpoint for 1 day (86400 seconds)
    if (bereq.url ~ "^/game") {
        set beresp.ttl = 86400s; # 1 day
        set beresp.grace = 5m; # If the object is stale, Varnish will keep it for another 5 minutes

        # Set cache-control headers to public to indicate that the response can be cached
        set beresp.http.cache-control = "public, max-age=86400";
    }

    if (bereq.url ~ "^/shop") {
        set beresp.ttl = 86400s; # 1 day
        set beresp.http.cache-control = "public, max-age=86400";
    }

    return (deliver);
}

# Called before a cached object is delivered to the client.
sub vcl_deliver {
    # Add headers to show whether the response was a cache hit or miss
    set resp.http.X-Cache-Provider = "Varnish Cache";
    if (obj.hits > 0) {
        set resp.http.X-Cache = "HIT";
    } else {
        set resp.http.X-Cache = "MISS";
    }

    # Remove any unwanted headers (optional)
    unset resp.http.Server;
    unset resp.http.X-Powered-By;
    unset resp.http.Via;
    unset resp.http.X-Varnish;
    unset resp.http.Age;

    return (deliver);
}