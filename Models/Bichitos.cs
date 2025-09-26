using System;

namespace Bichitos.Models;

public class Bichitos
{
    public string Codigo { get; set; }
    public string Color { get; set; }
    public DateTime Fecha { get; set; } = DateTime.Today;

    public override string ToString()
    {
        return $"Codigo: {Codigo}, Color: {Color}";
    }
}