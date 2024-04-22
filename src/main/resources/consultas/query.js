//Consulta ventas por producto
db.finances.aggregate([
    {
        $match: {
            fecha_venta: {
                $gte: ISODate("2023-01-01T00:00:00.000Z"),
                $lte: ISODate("2025-01-01T00:00:00.000Z")
            }
        }
    },
    {
        $unwind: "$detalle"
    },
    {
        $match: {
            "detalle.objeto.$id": 'RCC6'
        }
    },
    {
        $lookup: {
            from: "finances",
            localField: "detalle.objeto.$id",
            foreignField: "_id",
            as: "producto"
        }
    },
    {
        $group: {
            _id: {
                producto: "$detalle.objeto"
            },
            total_vendido: { $sum: "$detalle.cantidad" },
            total_monto: { $sum: "$detalle.monto" },
            articulo: { $first: "$producto"}
        }
    },
])