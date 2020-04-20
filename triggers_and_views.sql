/* 
    Vista para obtener el nombre del producto más vendido
*/
CREATE VIEW "PRODUCTO_MAS_VENDIDO" AS 
SELECT p.nombre
  FROM VentasDetalle vd
  INNER JOIN Productos p
  ON vd.idProducto = p.id
  GROUP BY p.nombre
  ORDER BY COUNT(*) DESC
  LIMIT 1;

/*
    Disparador que regresa al stock el producto una vez que se elimina un
    detalle de venta.
*/
DELIMITER;;
CREATE TRIGGER `deleteVentasDetalle` AFTER DELETE ON `ventasdetalle` FOR EACH ROW BEGIN
    UPDATE Productos
    SET stock = IF(stock <= -1, stock, stock + OLD.cantidad)
    WHERE id = OLD.idProducto;
END;;

/*
    Trigger que resta la cantidad en stock en Productos cada vez que una venta
    detalle es insertada, esto en caso de que el stock esté registrado.
*/
DELIMITER;;
CREATE TRIGGER `insertVentaDetalle` AFTER INSERT ON `ventasdetalle` FOR EACH ROW BEGIN
    UPDATE Productos
    SET stock = IF(stock <= 0, stock, stock - NEW.cantidad)
    WHERE id = NEW.idProducto;
END;;
DELIMITER;

/*
    Disparador que cambia el stock del Producto correspondiente a una venta
    detalle cada vez que esta se actualiza, esto en caso de que el stock esté
    registrado.
*/
DELIMITER;;
CREATE TRIGGER `updateVentasDetalle` BEFORE UPDATE ON `ventasdetalle` FOR EACH ROW BEGIN
    DECLARE valor_stock_a_cambiar DOUBLE;
    IF NEW.cantidad <> OLD.cantidad THEN
        IF NEW.cantidad > OLD.cantidad THEN
            SET @valor_stock_a_cambiar := NEW.cantidad - OLD.cantidad;

            UPDATE Productos
            SET stock = IF(stock <= -1, stock, stock + @valor_stock_a_cambiar)
            WHERE id = OLD.idProducto;
        ELSE
            SET @valor_stock_a_cambiar := OLD.cantidad - NEW.cantidad;

            UPDATE Productos
            SET stock = IF(stock <= -1, stock, stock - @valor_stock_a_cambiar)
            WHERE id = OLD.idProducto;
        END IF;
    END IF;
END;;
DELIMITER;

/* 
    Disparador que agrega a una tabla Devoluciones un registro correspondiente
    cada que se elimina una venta. FUNCIONALIDAD ACTUALMENTE NO IMPLEMENTADA.
*/
-- DELIMITER;;
-- CREATE TRIGGER `deleteVentas` AFTER DELETE ON `ventas` FOR EACH ROW BEGIN
--     INSERT INTO Devoluciones(total, idCajero)
--     VALUES (OLD.total, OLD.idCajero);
-- END;;
-- DELIMITER;