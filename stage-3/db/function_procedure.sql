CREATE OR REPLACE FUNCTION user_login_update(login VARCHAR, new_status VARCHAR) RETURNS VOID AS $$
BEGIN
    UPDATE Users SET status = new_status, last_login_date = CURRENT_DATE WHERE login = login;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_game_name(game_id INTEGER) RETURNS VARCHAR AS $$
DECLARE
    game_name VARCHAR;
BEGIN
    SELECT name INTO game_name FROM Games WHERE id = game_id;
    RETURN game_name;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION add_wallet_balance(wallet_id INTEGER, amount REAL) RETURNS VOID AS $$
BEGIN
    UPDATE Wallet SET balance = balance + amount WHERE id = wallet_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION library_entry_check() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM Library WHERE user_login = NEW.user_login AND game_id = NEW.game_id) THEN
        RAISE EXCEPTION 'User already owns this game in the library';
    END IF;


    -- Check if user has enough balance (Assuming price is available in Store table)
    IF (SELECT balance FROM Wallet WHERE id = (SELECT wallet_id FROM Users WHERE login = NEW.user_login)) < 
       (SELECT price FROM Store WHERE game_id = NEW.game_id) THEN
        RAISE EXCEPTION 'Insufficient funds';
    END IF;


    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER library_entry_trigger
BEFORE INSERT ON Library
FOR EACH ROW
EXECUTE FUNCTION library_entry_check();

CREATE OR REPLACE FUNCTION unique_game_in_store() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM Store WHERE game_id = NEW.game_id) THEN
        RAISE EXCEPTION 'This game already exists in the store';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger on Store table
CREATE TRIGGER unique_game_store_trigger
BEFORE INSERT ON Store
FOR EACH ROW
EXECUTE FUNCTION unique_game_in_store();

CREATE OR REPLACE FUNCTION unique_item_in_marketplace() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM Marketplace WHERE seller_login = NEW.seller_login AND item_id = NEW.item_id) THEN
        RAISE EXCEPTION 'This item is already listed by this seller in the marketplace';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER unique_item_marketplace_trigger
BEFORE INSERT ON Marketplace
FOR EACH ROW
EXECUTE FUNCTION unique_item_in_marketplace();

CREATE OR REPLACE FUNCTION check_sufficient_balance() RETURNS TRIGGER AS $$
DECLARE
    game_price REAL;
    user_balance REAL;
BEGIN
    SELECT price INTO game_price FROM Store WHERE game_id = NEW.game_id;
    SELECT balance INTO user_balance FROM Wallet WHERE id = (SELECT wallet_id FROM Users WHERE login = NEW.user_login);
    
    IF user_balance < game_price THEN
        RAISE EXCEPTION 'Insufficient funds to purchase this game';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_balance_before_add_to_library
BEFORE INSERT ON Library
FOR EACH ROW
EXECUTE FUNCTION check_sufficient_balance();

CREATE OR REPLACE FUNCTION deduct_balance_after_purchase() RETURNS TRIGGER AS $$
DECLARE
    game_price REAL;
BEGIN
    SELECT price INTO game_price FROM Store WHERE game_id = NEW.game_id;
    
    UPDATE Wallet
    SET balance = balance - game_price
    WHERE id = (SELECT wallet_id FROM Users WHERE login = NEW.user_login);
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER deduct_balance_on_purchase
AFTER INSERT ON Library
FOR EACH ROW
EXECUTE FUNCTION deduct_balance_after_purchase();

CREATE OR REPLACE FUNCTION check_item_ownership_for_sale() RETURNS TRIGGER AS $$
DECLARE
    item_quantity INTEGER;
BEGIN
    SELECT quantity INTO item_quantity FROM Inventory WHERE owner_login = NEW.seller_login AND item_id = NEW.item_id;
    
    IF item_quantity IS NULL OR item_quantity < 1 THEN
        RAISE EXCEPTION 'User does not own this item or has insufficient quantity';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_ownership_before_sale
BEFORE INSERT ON Marketplace
FOR EACH ROW
EXECUTE FUNCTION check_item_ownership_for_sale();

CREATE OR REPLACE FUNCTION add_balance_on_item_sale() RETURNS TRIGGER AS $$
DECLARE
    item_price REAL;
BEGIN
    SELECT price INTO item_price FROM Marketplace WHERE id = OLD.id;
    
    UPDATE Wallet
    SET balance = balance + item_price
    WHERE id = (SELECT wallet_id FROM Users WHERE login = OLD.seller_login);
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_wallet_balance_on_sale
AFTER DELETE ON Marketplace
FOR EACH ROW
EXECUTE FUNCTION add_balance_on_item_sale();

CREATE OR REPLACE FUNCTION update_last_played_date() RETURNS TRIGGER AS $$
BEGIN
    NEW.last_played_date := CURRENT_DATE;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_last_played_date_trigger
BEFORE UPDATE OF last_played_date ON Library
FOR EACH ROW
EXECUTE FUNCTION update_last_played_date();
