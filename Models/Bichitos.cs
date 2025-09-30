using System;
namespace Bichitos.Models;

public class Bichitos
{
    public string Color { get; set; }
    public DateTime Cumple { get; set; } = DateTime.Today;
    public string Nombre { get; set; }
    public bool Estado { get; set; } // SI ESTÁ VIVO O MUERTO
    public int evolucion { get; set; }
    

    public override string ToString()
    {
        return "Nombre: " + Nombre + ", Color: " + Color + ", Cumpleaños: " + Cumple + ", Evolucion: " + evolucion;
    }
}