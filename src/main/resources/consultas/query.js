//Consulta ventas por producto
db.finances.aggregate([
    {
        $match: {
            fecha_venta: {
                $gte: ISODate("2023-01-01T00:00:00.000Z"),
                $lte: ISODate("2025-01-01T00:00:00.000Z")
            },
            "realizada_en.$id": "00000",
            $or: [
                {
                    _class: "org.puig.puigapi.persistence.entity.finances.Venta"
                },
                {
                    _class: "org.puig.puigapi.persistence.entity.finances.Venta$Reparto"
                }
            ]
        }
    },
    {
        $unwind: "$ticket"
    },
    {
        $lookup: {
            from: "finances",
            localField: "ticket.detalle.$id",
            foreignField: "_id",
            as: "articulo"
        }
    },
    {
        $group: {
            _id: {
                producto: "$ticket.detalle"
            },
            total_vendido: { $sum: "$ticket.cantidad" },
            total_monto: { $sum: "$ticket.monto" },
            articulo: { $first: "articulo"}
        }
    },
])