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
        $unwind: "$ticket"
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
                producto: "$ticket.objeto"
            },
            total_vendido: { $sum: "$ticket.cantidad" },
            total_monto: { $sum: "$ticket.monto" },
            articulo: { $first: "$producto"}
        }
    },
])