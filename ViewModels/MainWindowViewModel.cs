using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using Avalonia.Animation;
using Avalonia.Controls;
using Avalonia.Media;
using Bichitos.Models;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;

namespace Bichitos.ViewModels;

public partial class MainWindowViewModel : ViewModelBase
{
    //decorador, privado, minuscula, sin get ni set (el observavbleproperty los implementa automaticamnete)
    [ObservableProperty]
    private string mensaje = "";
    
    [ObservableProperty] private bool avanzadas = false;

    [ObservableProperty] private ObservableCollection<Models.Bichitos> bichitos= new();
    
    [ObservableProperty] 
    private Models.Bichitos bichito = new Models.Bichitos();
    
    [ObservableProperty]
    private Models.Bichitos bichitoSeleccionado = new Models.Bichitos();

    public MainWindowViewModel()
    {
        CargarBichitos();
        CargarCombo();
    }

    [RelayCommand]
    public void ComprobarCumple()
    {
        CheckDate();
    }
    
    private bool CheckDate()
    {
        if (Bichito.Cumple > DateTime.Today)
        {
            Mensaje = "¿Aún no ha nacido?";
            return false;
        }
        else
        {
            Mensaje = "";
            return true;
        }
    }

    private void CargarBichitos()
    {
        Models.Bichitos bichito = new Models.Bichitos();
        bichito.Nombre = "Martín";
        bichito.Color = "Rosa";
        bichito.Cumple = Convert.ToDateTime("10/12/2003");
        bichito.Estado = true;
        bichito.evolucion = 4;
        Bichitos.Add(bichito);
        
        Models.Bichitos bichito2 = new Models.Bichitos();
        bichito2.Nombre = "Daniel";
        bichito2.Color = "Marrón";
        bichito2.Cumple = DateTime.Today;
        bichito.Estado = true;
        bichito.evolucion = 1;
        Bichitos.Add(bichito2);
    }

    [RelayCommand]
    public void CargarBichitoSeleccionado()
    {
        Bichito = BichitoSeleccionado;
    }
    
    public ObservableCollection<string> Lista { set; get; } = new()
    {
        "Rosa", "Fucsia", "Perla", "Topacio", "Rojo", "Marrón", "Negro"
    };
    
    public ObservableCollection<int> ListaEvoluciones { set; get; } = new()
    {
        1,2,3,4
    };
    
    public ObservableCollection<bool> ListaEstado { set; get; } = new()
    {
        true, false
    };
    
    private void CargarCombo()
    {
        Lista = new ObservableCollection<string>()
        {
            "Rosa", "Fucsia", "Perla", "Topacio", "Rojo", "Marrón", "Negro"
        };
        Bichito.Color = Lista[0];

        ListaEstado = new ObservableCollection<bool>()
        {
            true, false
        };
        Bichito.Estado = ListaEstado[0];

        ListaEvoluciones = new ObservableCollection<int>()
        {
            1,2,3,4
        };
        Bichito.evolucion = ListaEvoluciones[0];
    }
    
    [RelayCommand]
    public void EstadoInicialCheck(Object parameter)//cambia el color de las letras del check segun su estado
    {
        CheckBox check = (CheckBox)parameter;
        if (check.IsChecked == true)
        {
            check.Foreground=Brushes.Blue;
            check.FontWeight = FontWeight.Normal;
        }else{
            check.Foreground=Brushes.Red;
            check.FontWeight = FontWeight.Bold;
        }
    }
    
    
    
    [RelayCommand]
    public void MostrarBichito(Object parameter)
    {
        CheckBox check = (CheckBox)parameter;
        if (check.IsChecked == false)
        {
            Mensaje = "DONA PORFA";
            Console.WriteLine(Mensaje);
            return;
        }

        if (!CheckDate())
        {
            return;
        }

        Mensaje = "Nombre: " + Bichito.Nombre + ", Color: " + Bichito.Color + ", Cumpleaños: " + Bichito.Cumple + ", Evolucion: " + Bichito.evolucion;
        Console.WriteLine(Mensaje);
        Bichitos.Add(Bichito);
        Bichito = new Models.Bichitos();//para borrar los campos, para ello el modelo debe ser observable
        

    }
    
    [RelayCommand]
    public void MostrarOpcionesAvanzadas()
    {
        if (Avanzadas)
        {
            Avanzadas = false;
        }
        else
        {
            Avanzadas = true;
        }
    }
    
   
    

}

