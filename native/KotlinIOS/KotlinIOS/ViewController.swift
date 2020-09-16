import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet weak var departureStation: DropDown!
    @IBOutlet weak var arrivalStation: DropDown!
    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var departureStationLabel: UILabel!
    @IBOutlet weak var arrivalStationLabel: UILabel!
    @IBOutlet weak var getTimesButton: UIButton!
    @IBOutlet weak var journeyTable: UITableView!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    private var stationData: [Station] = [];
    private var journeyData: [Journey] = [];
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        getTimesButton.setTitle("Get Times", for: .normal)
        departureStationLabel.text = "Departure"
        arrivalStationLabel.text = "Arrival"
        label.text = "TrainBoard"
    }

    @IBAction func getTimes(_ sender: Any) {
        journeyData = []
        journeyTable.reloadData()
        loadingIndicator.startAnimating()
        
        presenter.onTimesRequested()
    }
}
 
extension ViewController : UITableViewDelegate, UITableViewDataSource {
    func formatDateTime(datetime: String) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "YYYY-MM-dd'T'HH:mm:ss.SSSXXX"
        guard let date: Date = dateFormatter.date(from: datetime) else {
            return datetime
        }
        
        let prettyDateFormatter = DateFormatter()
        prettyDateFormatter.dateFormat = "MMM d, h:mm a"
        return prettyDateFormatter.string(from: date)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return journeyData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "JourneyTableViewCell"
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? JourneyTableViewCell else {
            fatalError("The dequeued cell is not an instance of JourneyTableViewCell")
        }
        let journey = journeyData[indexPath.row]
        
        cell.departureTime.text = "Departs \(formatDateTime(datetime: journey.departureTime))"
        cell.arrivalTime.text = "Arrives \(formatDateTime(datetime: journey.arrivalTime))"
        cell.priceRange.text = "From £\(journey.minPrice / 100) to £\(journey.maxPrice / 100)"
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.onViewJourney(journey: journeyData[indexPath.row])
    }
}

extension ViewController: ApplicationContractView {
    func setStations(stations: [Station]) {
        stationData = stations;
            
        departureStation.optionArray = stationData.map{$0.description()}
        departureStation.didSelect{ (selectedText, index, id) in
            self.presenter.setDepartureStation(station: self.stationData[index])
        }
        arrivalStation.optionArray = stationData.map{$0.description()}
        arrivalStation.didSelect{ (selectedText, index, id) in
            self.presenter.setArrivalStation(station: self.stationData[index])
        }
    }
    
    func showAlert(message: String) {
        let alert = UIAlertController(title: "Alert", message: message, preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "Click", style: UIAlertAction.Style.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    func openUrl(url: String) {
        guard let link = URL(string: url) else {
          return //be safe
        }
        
        UIApplication.shared.open(link)
    }
    
    func setJourneys(journeys: [Journey]) {
        journeyData = journeys
        
        loadingIndicator.stopAnimating()
        journeyTable.reloadData()
    }
    
    func openJourneyView() {
        performSegue(withIdentifier: "showJourneyInfo", sender: nil)
    }
}
